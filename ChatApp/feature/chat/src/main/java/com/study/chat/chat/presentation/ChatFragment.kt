package com.study.chat.chat.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.clearFragmentResultListener
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.study.chat.R
import com.study.chat.chat.presentation.elm.ChatEffect
import com.study.chat.chat.presentation.elm.ChatEvent
import com.study.chat.chat.presentation.elm.ChatState
import com.study.chat.chat.presentation.model.UiEmoji
import com.study.chat.chat.presentation.model.UiMessage
import com.study.chat.chat.presentation.util.delegate.date.DateSeparatorDelegate
import com.study.chat.chat.presentation.util.delegate.message.MessageDelegate
import com.study.chat.chat.presentation.util.delegate.topic.TopicSeparatorDelegate
import com.study.chat.common.di.ChatComponentViewModel
import com.study.chat.common.domain.model.InvalidTopicTitleException
import com.study.chat.common.presentation.util.navigateToActionsFragment
import com.study.chat.common.presentation.util.navigateToChannelTopic
import com.study.chat.common.presentation.util.navigateToEmojiListFragment
import com.study.chat.common.presentation.util.setupSuggestionsAdapter
import com.study.chat.common.presentation.util.toErrorMessage
import com.study.chat.databinding.FragmentChatBinding
import com.study.common.ext.fastLazy
import com.study.components.customview.ScreenStateView.ViewState
import com.study.components.ext.delegatesToList
import com.study.components.ext.safeGetParcelable
import com.study.components.ext.showErrorSnackbar
import com.study.components.ext.showSnackbar
import com.study.components.recycler.SpaceVerticalDividerItemDecorator
import com.study.components.recycler.delegates.GeneralPaginationAdapterDelegate
import com.study.ui.NavConstants.CHANNEL_ID_KEY
import com.study.ui.NavConstants.CHANNEL_TITLE_KEY
import com.study.ui.NavConstants.TOPIC_COLOR_KEY
import com.study.ui.NavConstants.TOPIC_TITLE_KEY
import kotlinx.coroutines.launch
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.android.storeholder.StoreHolder
import javax.inject.Inject

internal class ChatFragment : ElmFragment<ChatEvent, ChatEffect, ChatState>() {
    private val binding: FragmentChatBinding get() = _binding!!
    private var _binding: FragmentChatBinding? = null
    private var adapter: GeneralPaginationAdapterDelegate? = null
    private val channelId: Int by fastLazy {
        arguments?.getInt(CHANNEL_ID_KEY) ?: error("Invalid channel id")
    }
    private val channelTitle: String by fastLazy {
        arguments?.getString(CHANNEL_TITLE_KEY) ?: error("Invalid channel title")
    }
    private val topicTitle: String? by fastLazy {
        arguments?.getString(TOPIC_TITLE_KEY)?.takeIf { title -> title != "{$TOPIC_TITLE_KEY}" }
    }
    private val topicColor: Int? by fastLazy {
        arguments?.getInt(TOPIC_COLOR_KEY)
    }
    private val selectFileLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                store.accept(
                    ChatEvent.Ui.UploadFile(
                        topicTitle = topicTitle ?: binding.fragmentChatEtTopicTitle.text.toString(),
                        uri = uri.toString()
                    )
                )
            }
        }
    override val initEvent: ChatEvent get() = ChatEvent.Ui.Init(channelId, topicTitle)
    override val storeHolder: StoreHolder<ChatEvent, ChatEffect, ChatState> get() = chatStore

    @Inject
    lateinit var chatStore: StoreHolder<ChatEvent, ChatEffect, ChatState>

    override fun onAttach(context: Context) {
        ViewModelProvider(this).get<ChatComponentViewModel>().chatComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        setupListeners()
        observeSearchQuery()
    }

    override fun onStop() {
        super.onStop()
        store.accept(ChatEvent.Ui.RemoveIrrelevantMessages)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clearFragmentResultListener(SELECTED_EMOJI_RESULT_KEY)
        adapter = null
        _binding = null
    }

    override fun render(state: ChatState) = with(binding) {
        when {
            state.isLoading -> screenStateView.setState(ViewState.Loading)
            state.error != null -> if (requireNotNull(adapter).snapshot().isEmpty()) {
                screenStateView.setState(ViewState.Error(state.error.toErrorMessage()))
            } else {
                screenStateView.setState(ViewState.Success)
                val customMessage = if (state.error is InvalidTopicTitleException) {
                    state.error.toErrorMessage().getDescription(requireContext())
                } else null
                showErrorSnackbar(
                    state.error,
                    Throwable::toErrorMessage,
                    customMessage = customMessage,
                )
            }
            else -> with(binding) {
                screenStateView.setState(ViewState.Success)
                lifecycleScope.launch { adapter?.submitData(state.messages as PagingData<Any>) }
                if (state.topics.isNotEmpty()) {
                    fragmentChatEtTopicTitle.setupSuggestionsAdapter(state.topics)
                }
            }
        }
    }

    override fun handleEffect(effect: ChatEffect) = when (effect) {
        is ChatEffect.ShowWarning ->
            binding.showErrorSnackbar(effect.error, Throwable::toErrorMessage)
        ChatEffect.FileUploaded ->
            binding.showSnackbar(R.string.success_uploading_file)
        is ChatEffect.UploadingFileError ->
            binding.showErrorSnackbar(effect.error, Throwable::toErrorMessage) {
                store.accept(ChatEvent.Ui.ReUploadFile)
            }
    }

    private fun setupListeners() = with(binding) {
        fragmentChatViewInputMessage.btnUploadContentListener =
            { selectFileLauncher.launch(ALL_CONTENT) }
        fragmentChatViewInputMessage.btnSendMessageClickListener = { input ->
            val topic = topicTitle ?: fragmentChatEtTopicTitle.text.toString()
            store.accept(ChatEvent.Ui.SendMessage(input, topic))
        }
        screenStateView.onTryAgainClickListener =
            View.OnClickListener { store.accept(ChatEvent.Ui.Reload) }
        setupPagingDataListener()
    }

    private fun setupPagingDataListener() = with(binding) {
        adapter?.addLoadStateListener { combinedState ->
            when (val state = combinedState.refresh) {
                LoadState.Loading -> screenStateView.setState(ViewState.Loading)
                is LoadState.NotLoading -> {
                    fragmentChatRvChat.isVisible = true
                    screenStateView.setState(ViewState.Success)
                }
                is LoadState.Error -> if (requireNotNull(adapter).itemCount > 0) {
                    screenStateView.setState(ViewState.Success)
                    showErrorSnackbar(state.error, Throwable::toErrorMessage) {
                        store.accept(ChatEvent.Ui.Reload)
                    }
                } else {
                    screenStateView.setState(ViewState.Error(state.error.toErrorMessage()))
                    fragmentChatRvChat.isVisible = false
                }

            }
        }
    }


    private fun initUI() {
        initDelegates()
        with(binding) {
            fragmentChatRvChat.run {
                addItemDecoration(SpaceVerticalDividerItemDecorator(CHAT_DIVIDER_SIZE))
                isNestedScrollingEnabled = false
                adapter = this@ChatFragment.adapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
            }

            if (topicTitle != null) {
                fragmentChatTvTopicTitle.text = getString(R.string.channel_topic_title, topicTitle)
                fragmentChatEtTopicTitle.isVisible = false
                topicColor?.let(fragmentChatTvTopicTitle::setBackgroundColor)
            } else {
                fragmentChatTvTopicTitle.isVisible = false
            }
        }
    }

    private fun initDelegates() {
        val messageDelegate = MessageDelegate(
            onLongClickListener = { messageId -> selectEmoji(messageId) },
            onAddReactionClickListener = {
                navigateToEmojiListFragment(SELECTED_EMOJI_RESULT_KEY)
            },
            onReactionClick = { mes, emoji ->
                store.accept(ChatEvent.Ui.UpdateReaction(emoji, mes))
            }
        )
        val delegates = if (topicTitle == null) {
            delegatesToList(
                messageDelegate,
                TopicSeparatorDelegate { topic ->
                    navigateToChannelTopic(channelId, channelTitle, topic)
                }
            )
        } else delegatesToList(messageDelegate, DateSeparatorDelegate())
        adapter = GeneralPaginationAdapterDelegate(delegates)
    }

    private fun observeSearchQuery() {
        binding.fragmentChatSearchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                store.accept(ChatEvent.Ui.Search(query.orEmpty()))
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                store.accept(ChatEvent.Ui.Search(newText.orEmpty()))
                return false
            }

        })
    }

    private fun selectEmoji(message: UiMessage) {
        setFragmentResultListener(SELECTED_EMOJI_RESULT_KEY) { _, bundle ->
            bundle.safeGetParcelable<UiEmoji>(SELECTED_EMOJI_RESULT_KEY)?.let { selectedEmoji ->
                store.accept(ChatEvent.Ui.UpdateReaction(selectedEmoji, message))
            }
        }
        navigateToActionsFragment(message.id, SELECTED_EMOJI_RESULT_KEY)
    }

    companion object {
        private const val SELECTED_EMOJI_RESULT_KEY = "selected-emoji-result"
        private const val ALL_CONTENT = "*/*"
        private const val CHAT_DIVIDER_SIZE = 2
    }
}
