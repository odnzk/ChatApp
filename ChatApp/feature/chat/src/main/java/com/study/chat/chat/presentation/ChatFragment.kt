package com.study.chat.chat.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.clearFragmentResultListener
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
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
import com.study.chat.databinding.FragmentChatBinding
import com.study.chat.shared.di.ChatComponentViewModel
import com.study.chat.shared.domain.model.InvalidTopicTitleException
import com.study.chat.shared.presentation.util.navigateToActionsFragment
import com.study.chat.shared.presentation.util.navigateToChannelTopic
import com.study.chat.shared.presentation.util.setupSuggestionsAdapter
import com.study.chat.shared.presentation.util.toErrorMessage
import com.study.common.extension.fastLazy
import com.study.common.search.NothingFoundForThisQueryException
import com.study.components.extension.collectFlowSafely
import com.study.components.extension.delegatesToList
import com.study.components.extension.safeGetParcelable
import com.study.components.extension.showErrorSnackbar
import com.study.components.extension.showSnackbar
import com.study.components.recycler.decorator.SpaceVerticalDividerItemDecorator
import com.study.components.recycler.delegates.GeneralPaginationAdapterDelegate
import com.study.components.view.ScreenStateView.ViewState
import com.study.ui.NavConstants.CHANNEL_ID_KEY
import com.study.ui.NavConstants.CHANNEL_TITLE_KEY
import com.study.ui.NavConstants.TOPIC_TITLE_KEY
import kotlinx.coroutines.flow.Flow
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
        val argsTitle = arguments?.getString(TOPIC_TITLE_KEY)
        if (argsTitle != null && argsTitle != "{$TOPIC_TITLE_KEY}") argsTitle else null
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

    @Inject
    lateinit var searchFlow: Flow<String>

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

    override fun render(state: ChatState) {
        when {
            state.isLoading -> binding.screenStateView.setState(ViewState.Loading)
            state.error != null -> {
                binding.screenStateView.setState(ViewState.Success)
                val customMessage = if (state.error is InvalidTopicTitleException) {
                    state.error.toErrorMessage().getDescription(requireContext())
                } else null
                binding.showErrorSnackbar(
                    state.error,
                    Throwable::toErrorMessage,
                    customMessage = customMessage,
                )
            }
            else -> {
                binding.screenStateView.setState(ViewState.Success)
                lifecycleScope.launch { adapter?.submitData(state.messages as PagingData<Any>) }
                if (state.topics.isNotEmpty()) {
                    binding.fragmentChatEtTopicTitle.setupSuggestionsAdapter(state.topics)
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

    private fun setupListeners() {
        with(binding) {
            fragmentChatViewInputMessage.btnSubmitClickListener = { input ->
                if (input.isEmpty()) {
                    selectFileLauncher.launch(ALL_CONTENT)
                } else {
                    val topic = topicTitle ?: fragmentChatEtTopicTitle.text.toString()
                    store.accept(ChatEvent.Ui.SendMessage(input, topic))
                }
            }
            screenStateView.onTryAgainClickListener = View.OnClickListener {
                store.accept(ChatEvent.Ui.Reload)
            }
            adapter?.addLoadStateListener { combinedState ->
                when (val state = combinedState.refresh) {
                    LoadState.Loading -> screenStateView.setState(ViewState.Loading)
                    is LoadState.NotLoading -> {
                        fragmentChatRvChat.isVisible = true
                        val screenState = if (adapter?.snapshot()?.isEmpty() == true) {
                            ViewState.Error(
                                NothingFoundForThisQueryException().toErrorMessage(),
                                false
                            )
                        } else ViewState.Success
                        screenStateView.setState(screenState)
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
            } else {
                fragmentChatTvTopicTitle.isVisible = false
            }
        }
        (activity as? AppCompatActivity)?.supportActionBar?.let {
            it.title = getString(R.string.channel_title, channelTitle)
        }
    }

    private fun observeSearchQuery() {
        collectFlowSafely(searchFlow, Lifecycle.State.RESUMED) {
            store.accept(ChatEvent.Ui.Search(it))
        }
    }

    private fun selectEmoji(message: UiMessage) {
        setFragmentResultListener(SELECTED_EMOJI_RESULT_KEY) { _, bundle ->
            bundle.safeGetParcelable<UiEmoji>(SELECTED_EMOJI_RESULT_KEY)?.let { selectedEmoji ->
                store.accept(ChatEvent.Ui.UpdateReaction(selectedEmoji, message))
            }
        }
        navigateToActionsFragment(message.id, SELECTED_EMOJI_RESULT_KEY)
    }

    private fun initDelegates() {
        val messageDelegate = MessageDelegate(
            onLongClickListener = { messageId -> selectEmoji(messageId) },
            onAddReactionClickListener = { messageId -> selectEmoji(messageId) },
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

    companion object {
        private const val SELECTED_EMOJI_RESULT_KEY = "selected-emoji-result"
        private const val ALL_CONTENT = "*/*"
        private const val CHAT_DIVIDER_SIZE = 2
    }
}