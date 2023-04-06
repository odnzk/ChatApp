package com.study.chat.domain.usecase

import com.study.chat.domain.model.OutcomeMessage
import com.study.chat.domain.repository.MessageRepository
import com.study.chat.presentation.chat.model.UiMessage
import com.study.common.extensions.isSameDay
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class SendMessageUseCase(
    private val repository: MessageRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        channelTitle: String,
        messageContent: String,
        topicTitle: String,
        list: List<Any>
    ): List<Any> =
        withContext(dispatcher) {
            val meMessage = UiMessage.MeMessage(messageContent)
            val message = OutcomeMessage(
                channelTitle = channelTitle,
                content = messageContent,
                topicTitle = topicTitle
            )
            meMessage.id = repository.sendMessage(message)
            val lastMessageDate = list.filterIsInstance<UiMessage>().last().calendar
            if (lastMessageDate.isSameDay(meMessage.calendar)) {
                list.toMutableList().apply { add(meMessage) }
            } else {
                list.toMutableList().apply {
                    add(meMessage.calendar)
                    add(meMessage)
                }
            }
        }
}
