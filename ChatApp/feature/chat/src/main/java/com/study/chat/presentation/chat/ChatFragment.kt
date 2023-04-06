package com.study.chat.presentation.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.clearFragmentResultListener
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.study.chat.presentation.chat.util.delegates.date.DateDelegate
import com.study.chat.presentation.chat.util.delegates.date.DateViewHolder
import com.study.chat.presentation.chat.util.delegates.message.MessageDelegate
import com.study.chat.presentation.chat.util.navigation.navigateToEmojiListFragment
import com.study.common.ScreenState
import com.study.components.BaseFragment
import com.study.components.recycler.decorator.StickyHeaderItemDecoration
import com.study.components.recycler.delegates.Delegate
import com.study.components.recycler.delegates.GeneralPaginationAdapterDelegate
import com.study.feature.databinding.FragmentChatBinding

internal class ChatFragment : BaseFragment<ChatViewModel, FragmentChatBinding>() {
    override val viewModel: ChatViewModel by viewModels()
    override val binding: FragmentChatBinding get() = _binding!!
    private var _binding: FragmentChatBinding? = null
    private var adapter: GeneralPaginationAdapterDelegate? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clearFragmentResultListener(SELECTED_EMOJI_RESULT_KEY)
        adapter = null
        _binding = null
    }

    override fun setupListeners() {
        with(binding) {
            fragmentChatViewInputMessage.btnSubmitClickListener = { inputText ->
                viewModel.onEvent(
                    ChatFragmentEvent.SendMessage(
                        messageContent = inputText,
                        requireNotNull(adapter).snapshot().toList()
                    )
                )
            }
        }
        adapter?.addLoadStateListener { combinedState ->
            val screenState = when (val state = combinedState.refresh) {
                LoadState.Loading -> ScreenState.Loading
                is LoadState.NotLoading -> ScreenState.Success(Unit)
                is LoadState.Error -> ScreenState.Error(state.error)
            }
            binding.fragmentChatScreenStateView.setState(screenState)
        }
    }

    override fun observeState() {
        viewModel.state.collectFlowSafely { pagingData ->
            adapter?.submitData(pagingData)
        }
    }

    override fun initUI() {
        val messageDelegate =
            MessageDelegate(onLongClickListener = { messageId -> selectEmoji(messageId) },
                onAddReactionClickListener = { messageId -> selectEmoji(messageId) },
                onReactionClick = { messageId, emojiName ->
                    viewModel.onEvent(
                        ChatFragmentEvent.UpdateReaction(
                            messageId, emojiName, requireNotNull(adapter).snapshot()
                        )
                    )
                })
        adapter = GeneralPaginationAdapterDelegate(
            listOf(
                messageDelegate, DateDelegate()
            ) as List<Delegate<RecyclerView.ViewHolder, Any>>
        )
        with(binding.fragmentChatRvChat) {
            addItemDecoration(StickyHeaderItemDecoration<DateViewHolder>())
            isNestedScrollingEnabled = false
            adapter = this@ChatFragment.adapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun selectEmoji(messageId: Int) {
        setFragmentResultListener(SELECTED_EMOJI_RESULT_KEY) { _, bundle ->
            bundle.getString(SELECTED_EMOJI_RESULT_KEY)?.let { selectedEmojiName ->
                viewModel.onEvent(
                    ChatFragmentEvent.UpdateReaction(
                        messageId,
                        selectedEmojiName,
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
