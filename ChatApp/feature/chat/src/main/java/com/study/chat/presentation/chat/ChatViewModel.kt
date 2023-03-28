package com.study.chat.presentation.chat

import com.study.chat.data.StubMessageRepository
import com.study.chat.domain.model.MessageType
import com.study.chat.domain.model.OutcomeMessage
import com.study.chat.domain.repository.MessageRepository
import com.study.chat.presentation.chat.mapper.toUiMessages
import com.study.chat.presentation.chat.model.UiMessage
import com.study.components.BaseViewModel
import com.study.components.ScreenState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map

internal class ChatViewModel : BaseViewModel<List<UiMessage>>() {
    private val messageRepository: MessageRepository = StubMessageRepository()
    private var jobObservingMessages: Job? = null

    override val _state: MutableStateFlow<ScreenState<List<UiMessage>>> =
        MutableStateFlow(ScreenState.Loading)
    val state = _state.asStateFlow()

    init {
        loadData()
    }

    fun onEvent(event: ChatFragmentEvent) {
        safeLaunch {
            when (event) {
                ChatFragmentEvent.Reload -> loadData()
                is ChatFragmentEvent.SendMessage -> {
                    val testMessage =
                        OutcomeMessage(MessageType.PRIVATE, 1, event.messageContent, topic = null)
                    messageRepository.sendMessage(testMessage)
                }
                is ChatFragmentEvent.UpdateReaction -> {
                    messageRepository.addReaction(event.messageId, event.emojiName)
                }
            }
        }

    }

    private fun loadData() {
        jobObservingMessages?.cancel()
        jobObservingMessages = safeLaunch {
            messageRepository.getMessages().handleErrors().map { it.toUiMessages() }
                .collectLatest { uiMessages ->
                    _state.value = ScreenState.Success(uiMessages)
                }
        }
    }
}
