package com.study.tinkoff.feature.chat.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.clearFragmentResultListener
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.study.common.rv.decorator.StickyHeaderItemDecoration
import com.study.common.rv.delegates.Delegate
import com.study.common.rv.delegates.GeneralAdapterDelegate
import com.study.components.BaseScreenStateFragment
import com.study.components.view.ScreenStateView
import com.study.tinkoff.databinding.FragmentChatBinding
import com.study.tinkoff.feature.chat.presentation.delegates.date.DateDelegate
import com.study.tinkoff.feature.chat.presentation.delegates.date.DateViewHolder
import com.study.tinkoff.feature.chat.presentation.delegates.message.MessageDelegate
import com.study.tinkoff.feature.chat.presentation.model.UiMessage
import com.study.tinkoff.feature.chat.presentation.navigation.toSelectEmojiFragment
import java.util.Calendar

class ChatFragment :
    BaseScreenStateFragment<ChatViewModel, FragmentChatBinding, List<UiMessage>>() {
    private var _binding: FragmentChatBinding? = null
    override val viewModel: ChatViewModel by viewModels()
    override val binding: FragmentChatBinding get() = _binding!!
    override val screenStateView: ScreenStateView get() = binding.fragmentChatScreenStateView
    private var chatAdapter: GeneralAdapterDelegate? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clearFragmentResultListener(SELECTED_EMOJI_RESULT_KEY)
        chatAdapter = null
        _binding = null
    }

    override fun observeState() {
        viewModel.state.collectScreenStateSafely()
    }

    override fun setupListeners() {
        with(binding) {
            fragmentChatViewInputMessage.btnSubmitClickListener = { inputText ->
                viewModel.onEvent(ChatFragmentEvent.SendMessage(inputText))
            }
        }
    }

    override fun initUI() {
        chatAdapter = GeneralAdapterDelegate(
            listOf(
                DateDelegate(), MessageDelegate(onLongClickListener = { messageId ->
                    selectEmoji(messageId)
                },
                    onAddReactionClickListener = { messageId -> selectEmoji(messageId) },
                    onReactionClick = { messageId, emojiName ->
                        viewModel.onEvent(
                            ChatFragmentEvent.UpdateReaction(
                                messageId, emojiName
                            )
                        )
                    })
            ) as List<Delegate<RecyclerView.ViewHolder, Any>>
        )
        with(binding.fragmentChatRvChat) {
            addItemDecoration(StickyHeaderItemDecoration<DateViewHolder>())
            isNestedScrollingEnabled = false
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onError(error: Throwable) = Unit
    override fun onLoading() = Unit

    override fun onSuccess(data: List<UiMessage>) {
        chatAdapter?.submitList(data.toMessageWithDateGrouping())
    }

    private fun selectEmoji(messageId: Int) {
        setFragmentResultListener(SELECTED_EMOJI_RESULT_KEY) { _, bundle ->
            bundle.getString(SELECTED_EMOJI_RESULT_KEY)?.let { selectedEmojiName ->
                viewModel.onEvent(ChatFragmentEvent.UpdateReaction(messageId, selectedEmojiName))
            }
        }
        findNavController().toSelectEmojiFragment(SELECTED_EMOJI_RESULT_KEY)
    }

    private fun List<UiMessage>.toMessageWithDateGrouping(): List<Any> {
        if (isEmpty()) return emptyList()
        val resultList = mutableListOf<Any>()
        var lastAddedDate: Calendar = Calendar.getInstance().apply { timeInMillis = 0 }
        for (message in sortedBy { it.calendar.time }) {
            if (lastAddedDate.get(Calendar.DATE) != message.calendar.get(Calendar.DATE) || lastAddedDate.get(
                    Calendar.MONTH
                ) != message.calendar.get(Calendar.MONTH) || lastAddedDate.get(Calendar.YEAR) != message.calendar.get(
                    Calendar.YEAR
                )
            ) {
                lastAddedDate = message.calendar
                resultList.add(lastAddedDate)
            }
            resultList.add(message)
        }
        return resultList
    }

    companion object {
        private const val SELECTED_EMOJI_RESULT_KEY = "selected-emoji-result"
    }
}
