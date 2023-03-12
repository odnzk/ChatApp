package com.study.tinkoff.feature.chat.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.clearFragmentResultListener
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.study.tinkoff.R
import com.study.tinkoff.core.ui.BaseScreenStateFragment
import com.study.tinkoff.core.ui.rv.delegates.MainAdapter
import com.study.tinkoff.databinding.FragmentChatBinding
import com.study.tinkoff.feature.chat.presentation.delegates.date.DateDelegateAdapter
import com.study.tinkoff.feature.chat.presentation.delegates.message.MessageDelegateAdapter
import com.study.tinkoff.feature.chat.presentation.delegates.message.toMessageDelegateItemsWithDateGrouping
import com.study.tinkoff.feature.chat.presentation.model.UiMessage
import com.study.tinkoff.feature.chat.presentation.navigation.toSelectEmojiFragment

class ChatFragment :
    BaseScreenStateFragment<ChatViewModel, FragmentChatBinding, List<UiMessage>>() {
    private var _binding: FragmentChatBinding? = null
    override val viewModel: ChatViewModel by viewModels()
    override val binding: FragmentChatBinding get() = _binding!!

    private var chatAdapter: MainAdapter? = MainAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun observeState() {
        viewModel.state.collectStateFlowSafely()
    }

    override fun setupListeners() {
        with(binding) {
            fragmentChatEtMessage.addTextChangedListener {
                val resId = if (it.isNullOrBlank()) {
                    R.drawable.ic_round_add
                } else {
                    R.drawable.baseline_send_24
                }
                fragmentChatBtnSendMessage.setIconResource(resId)
            }

            fragmentChatBtnSendMessage.setOnClickListener {
                val text = fragmentChatEtMessage.text.toString()
                fragmentChatEtMessage.setText("")
                viewModel.onEvent(ChatFragmentEvent.SendMessage(text))
            }
        }
    }

    override fun initUI() {
        chatAdapter?.apply {
            addDelegate(DateDelegateAdapter())
            val messageDelegateAdapter = MessageDelegateAdapter().apply {
                onLongClickListener = { messageId -> selectEmoji(messageId) }
                onAddReactionClickListener = { messageId -> selectEmoji(messageId) }
                onReactionClick = { messageId, emojiName ->
                    viewModel.onEvent(ChatFragmentEvent.UpdateReaction(messageId, emojiName))
                }
            }
            addDelegate(messageDelegateAdapter)
        }
        with(binding.fragmentChatRvChat) {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(context)
        }
        with(binding.fragmentChatRvChat) {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clearFragmentResultListener(SELECTED_EMOJI_RESULT)
        chatAdapter = null
        _binding = null
    }

    override fun onError(error: Throwable) = Unit
    override fun onLoading() = Unit

    override fun onSuccess(data: List<UiMessage>) {
        chatAdapter?.submitList(data.toMessageDelegateItemsWithDateGrouping())
    }

    private fun selectEmoji(messageId: Int) {
        setFragmentResultListener(SELECTED_EMOJI_RESULT) { _, bundle ->
            bundle.getString(SELECTED_EMOJI_NAME_KEY)?.let { selectedEmojiName ->
                viewModel.onEvent(ChatFragmentEvent.UpdateReaction(messageId, selectedEmojiName))
            }
        }
        findNavController().toSelectEmojiFragment()
    }

    companion object {
        const val SELECTED_EMOJI_RESULT = "selected-emoji-result"
        const val SELECTED_EMOJI_NAME_KEY = "selected-emoji"
    }
}
