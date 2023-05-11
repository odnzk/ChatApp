package com.study.chat.presentation.chat

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.study.chat.databinding.FragmentChatBinding
import com.study.chat.di.ChatComponentViewModel
import com.study.chat.domain.exceptions.InvalidTopicTitleException
import com.study.chat.presentation.util.model.UiEmoji
import com.study.chat.presentation.chat.elm.ChatEffect
import com.study.chat.presentation.chat.elm.ChatEvent
import com.study.chat.presentation.chat.elm.ChatState
import com.study.chat.presentation.chat.util.delegate.date.DateSeparatorDelegate
import com.study.chat.presentation.chat.util.delegate.message.MessageDelegate
import com.study.chat.presentation.chat.util.delegate.topic.TopicSeparatorDelegate
import com.study.chat.presentation.chat.util.model.UiMessage
import com.study.chat.presentation.util.navigateToActionsFragment
import com.study.chat.presentation.util.navigateToChannelTopic
import com.study.chat.presentation.util.setupSuggestionsAdapter
import com.study.chat.presentation.util.toErrorMessage
import com.study.common.extension.fastLazy
import com.study.components.extension.collectFlowSafely
import com.study.components.extension.createStoreHolder
import com.study.components.extension.delegatesToList
import com.study.components.extension.safeGetParcelable
import com.study.components.extension.showErrorSnackbar
import com.study.components.recycler.delegates.GeneralPaginationAdapterDelegate
import com.study.components.view.ScreenStateView.ViewState
import com.study.ui.NavConstants.CHANNEL_ID_KEY
import com.study.ui.NavConstants.TOPIC_TITLE_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.android.storeholder.StoreHolder
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

internal class ChatFragment : ElmFragment<ChatEvent, ChatEffect, ChatState>() {
    private val binding: FragmentChatBinding get() = _binding!!
    private var _binding: FragmentChatBinding? = null
    private var adapter: GeneralPaginationAdapterDelegate? = null

    private val channelId: Int by fastLazy {
        arguments?.getInt(CHANNEL_ID_KEY) ?: error("Invalid channel")
    }
    private val topicTitle: String? by fastLazy {
        val argsTitle = arguments?.getString(TOPIC_TITLE_KEY)
        if (argsTitle != null && argsTitle != "{$TOPIC_TITLE_KEY}") argsTitle else null
    }
    override val initEvent: ChatEvent get() = ChatEvent.Ui.Init(channelId, topicTitle)

    @Inject
    lateinit var chatStore: Store<ChatEvent, ChatEffect, ChatState>

    @Inject
    lateinit var searchFlow: Flow<String>

    override val storeHolder: StoreHolder<ChatEvent, ChatEffect, ChatState> by fastLazy {
        createStoreHolder(chatStore)
    }

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
                val customMessage = if (state.error is InvalidTopicTitleException) {
                    state.error.toErrorMessage().getDescription(requireContext())
                } else null
                showErrorSnackbar(
                    binding.root,
                    state.error,
                    Throwable::toErrorMessage,
                    customMessage = customMessage,
                )
            }
            else -> {
                lifecycleScope.launch { adapter?.submitData(state.messages as PagingData<Any>) }
                if (state.topics.isNotEmpty()) {
                    binding.fragmentChatEtTopicTitle.setupSuggestionsAdapter(state.topics)
                }
            }
        }
    }

    override fun handleEffect(effect: ChatEffect) {
        when (effect) {
            is ChatEffect.ShowWarning -> {
                showErrorSnackbar(binding.root, effect.error, Throwable::toErrorMessage)
            }
        }
    }

    private fun setupListeners() {
        with(binding) {
            fragmentChatViewInputMessage.btnSubmitClickListener = { input ->
                val topic = topicTitle ?: fragmentChatEtTopicTitle.text.toString()
                store.accept(ChatEvent.Ui.SendMessage(input, topic))
            }
            screenStateView.onTryAgainClickListener = View.OnClickListener {
                store.accept(ChatEvent.Ui.Reload)
            }
        }
        adapter?.addLoadStateListener { combinedState ->
            when (val state = combinedState.refresh) {
                LoadState.Loading -> binding.screenStateView.setState(ViewState.Loading)
                is LoadState.NotLoading -> with(binding) {
                    screenStateView.setState(ViewState.Success)
                    fragmentChatRvChat.isVisible = true
                }
                is LoadState.Error ->
                    if (requireNotNull(adapter).itemCount > 0) {
                        binding.screenStateView.setState(ViewState.Success)
                        showErrorSnackbar(binding.root, state.error, Throwable::toErrorMessage)
                    } else with(binding) {
                        fragmentChatRvChat.isVisible = false
                        screenStateView.setState(ViewState.Error(state.error.toErrorMessage()))
                    }
            }
        }
    }


    private fun initUI() {
        initChatAdapter()
        with(binding) {
            fragmentChatRvChat.run {
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

    private fun initChatAdapter() {
        val messageDelegate = MessageDelegate(
            onLongClickListener = { messageId -> selectEmoji(messageId) },
            onAddReactionClickListener = { messageId -> selectEmoji(messageId) },
            onReactionClick = { mes, emoji ->
                store.accept(
                    ChatEvent.Ui.UpdateReaction(
                        emoji,
                        mes
                    )
                )
            }
        )
        adapter = if (topicTitle == null) {
            GeneralPaginationAdapterDelegate(
                delegatesToList(
                    messageDelegate,
                    TopicSeparatorDelegate(onTopicClick = { selectedTopicTitle ->
                        navigateToChannelTopic(channelId, selectedTopicTitle)
                    })
                )
            )
        } else {
            GeneralPaginationAdapterDelegate(
                delegatesToList(messageDelegate, DateSeparatorDelegate())
            )
        }
    }

    companion object {
        private const val SELECTED_EMOJI_RESULT_KEY = "selected-emoji-result"
    }
}
