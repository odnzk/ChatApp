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
import androidx.recyclerview.widget.LinearLayoutManager
import com.study.chat.di.ChatComponentViewModel
import com.study.chat.domain.model.Emoji
import com.study.chat.presentation.chat.elm.ChatEffect
import com.study.chat.presentation.chat.elm.ChatEvent
import com.study.chat.presentation.chat.elm.ChatState
import com.study.chat.presentation.chat.util.delegates.date.DateDelegate
import com.study.chat.presentation.chat.util.delegates.message.MessageDelegate
import com.study.chat.presentation.chat.util.model.UiMessage
import com.study.chat.presentation.util.navigation.navigateToEmojiListFragment
import com.study.chat.presentation.util.toErrorMessage
import com.study.common.extensions.fastLazy
import com.study.components.extensions.collectFlowSafely
import com.study.components.extensions.createStoreHolder
import com.study.components.extensions.delegatesToList
import com.study.components.extensions.safeGetParcelable
import com.study.components.recycler.delegates.GeneralPaginationAdapterDelegate
import com.study.components.view.ScreenStateView.ViewState
import com.study.feature.R
import com.study.feature.databinding.FragmentChatBinding
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

    private val selectedChannelTitle: String by fastLazy {
        arguments?.getString("channelTitle") ?: error("Invalid channel title")
    }
    private val selectedTopicTitle: String by fastLazy {
        arguments?.getString("topicTitle") ?: error("Invalid topic title")
    }
    override val initEvent: ChatEvent
        get() = ChatEvent.Ui.Init(selectedChannelTitle, selectedTopicTitle)

    @Inject
    lateinit var chatStore: Store<ChatEvent, ChatEffect, ChatState>

    @Inject
    lateinit var searchFlow: Flow<String>

    override val storeHolder: StoreHolder<ChatEvent, ChatEffect, ChatState> by fastLazy {
        createStoreHolder(chatStore)
    }

    override fun onAttach(context: Context) {
        ViewModelProvider(this).get<ChatComponentViewModel>()
            .chatComponent.inject(this)
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

    override fun onDestroyView() {
        super.onDestroyView()
        clearFragmentResultListener(SELECTED_EMOJI_RESULT_KEY)
        adapter = null
        _binding = null
    }

    override fun render(state: ChatState) {
        when {
            state.isLoading -> binding.screenStateView.setState(ViewState.Loading)
            else -> {
                binding.screenStateView.setState(ViewState.Success)
                lifecycleScope.launch {
                    adapter?.submitData(state.messages)
                }
            }
        }
    }

    override fun handleEffect(effect: ChatEffect) {
        when (effect) {
            is ChatEffect.ShowWarning -> {
                with(binding) {
                    screenStateView.setState(ViewState.Error(effect.error.toErrorMessage()))
                    fragmentChatRvChat.isVisible = false
                    fragmentChatViewInputMessage.isVisible = false
                }
            }
        }
    }

    private fun setupListeners() {
        with(binding) {
            fragmentChatViewInputMessage.btnSubmitClickListener = { input ->
                store.accept(ChatEvent.Ui.SendMessage(input, requireNotNull(adapter).snapshot()))
            }
            screenStateView.onTryAgainClickListener = View.OnClickListener {
                store.accept(ChatEvent.Ui.Reload)
            }
        }
        adapter?.addLoadStateListener { combinedState ->
            val screenState = when (val state = combinedState.refresh) {
                LoadState.Loading -> ViewState.Loading
                is LoadState.NotLoading -> ViewState.Success
                is LoadState.Error -> ViewState.Error(state.error.toErrorMessage())
            }
            binding.screenStateView.setState(screenState)
        }
    }


    private fun initUI() {
        val messageDelegate = MessageDelegate(
            onLongClickListener = { messageId -> selectEmoji(messageId) },
            onAddReactionClickListener = { messageId -> selectEmoji(messageId) },
            onReactionClick = { message, emoji ->
                store.accept(
                    ChatEvent.Ui.UpdateReaction(message, emoji, requireNotNull(adapter).snapshot())
                )
            }
        )
        adapter = GeneralPaginationAdapterDelegate(delegatesToList(messageDelegate, DateDelegate()))
        with(binding) {
            fragmentChatRvChat.run {
                isNestedScrollingEnabled = false
                adapter = this@ChatFragment.adapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
            }
            fragmentChatTvTopicTitle.text =
                getString(R.string.channel_topic_title, selectedTopicTitle)
        }
    }

    private fun observeSearchQuery() {
        collectFlowSafely(searchFlow, Lifecycle.State.RESUMED) {
            store.accept(ChatEvent.Ui.Search(it))
        }
    }

    private fun selectEmoji(message: UiMessage) {
        setFragmentResultListener(SELECTED_EMOJI_RESULT_KEY) { _, bundle ->
            bundle.safeGetParcelable<Emoji>(SELECTED_EMOJI_RESULT_KEY)?.let { selectedEmoji ->
                store.accept(
                    ChatEvent.Ui.UpdateReaction(
                        message,
                        selectedEmoji,
                        requireNotNull(adapter).snapshot()
                    )
                )
            }
        }
        navigateToEmojiListFragment(SELECTED_EMOJI_RESULT_KEY)
    }

    companion object {
        private const val SELECTED_EMOJI_RESULT_KEY = "selected-emoji-result"
    }
}
