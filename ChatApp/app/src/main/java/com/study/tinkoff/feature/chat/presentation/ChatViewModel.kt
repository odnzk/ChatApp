package com.study.tinkoff.feature.chat.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.study.tinkoff.core.domain.model.message.MessageType
import com.study.tinkoff.core.domain.model.message.OutcomeMessage
import com.study.tinkoff.core.ui.ScreenState
import com.study.tinkoff.feature.chat.data.repository.StubMessageRepository
import com.study.tinkoff.feature.chat.domain.repository.MessageRepository
import com.study.tinkoff.feature.chat.presentation.model.UiMessage
import com.study.tinkoff.feature.chat.presentation.util.toUiMessages
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    private val messageRepository: MessageRepository = StubMessageRepository

    private val _state: MutableStateFlow<ScreenState<List<UiMessage>>> =
        MutableStateFlow(ScreenState.Loading)
    val state = _state.asStateFlow()

    init {
        loadData()
    }

    fun onEvent(event: ChatFragmentEvent) = viewModelScope.launch {
        when (event) {
            ChatFragmentEvent.ReloadData -> loadData()
            is ChatFragmentEvent.SendMessage -> {
                val testMessage =
                    OutcomeMessage(MessageType.PRIVATE, 1, event.messageContent, topic = null)
                messageRepository.sendMessage(testMessage)
            }
            is ChatFragmentEvent.UpdateReaction -> {
                messageRepository.addReaction(event.messageId, event.emojiName)
            }
        }
        loadData()
    }

    private fun loadData() = viewModelScope.launch {
        messageRepository.getMessages().toUiMessages().also { uiMessages ->
            _state.value = ScreenState.Success(uiMessages)
        }
    }
}
