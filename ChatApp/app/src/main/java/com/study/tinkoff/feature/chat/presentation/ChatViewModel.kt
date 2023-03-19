package com.study.tinkoff.feature.chat.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.study.components.ScreenState
import com.study.domain.model.MessageType
import com.study.domain.model.OutcomeMessage
import com.study.tinkoff.di.StubDiContainer
import com.study.tinkoff.feature.chat.domain.repository.MessageRepository
import com.study.tinkoff.feature.chat.presentation.model.UiMessage
import com.study.tinkoff.feature.chat.presentation.util.toUiMessages
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    private val messageRepository: MessageRepository = StubDiContainer.bindsMessageRepository()

    private val _state: MutableStateFlow<ScreenState<List<UiMessage>>> =
        MutableStateFlow(ScreenState.Loading)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            loadData()
        }
    }

    fun onEvent(event: ChatFragmentEvent) = viewModelScope.launch {
        when (event) {
            ChatFragmentEvent.ReloadData -> loadData()
            is ChatFragmentEvent.SendMessage -> {
                val testMessage =
                    OutcomeMessage(MessageType.PRIVATE, 1, event.messageContent, topic = null)
                messageRepository.sendMessage(testMessage)
                loadData()
            }
            is ChatFragmentEvent.UpdateReaction -> {
                messageRepository.addReaction(event.messageId, event.emojiName)
                loadData()
            }
        }
    }

    private suspend fun loadData() =
        messageRepository.getMessages().toUiMessages().also { uiMessages ->
            _state.value = ScreenState.Success(uiMessages)
        }

}
