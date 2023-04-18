package com.study.chat.domain.usecase

import com.study.chat.domain.model.OutcomeMessage
import com.study.chat.domain.repository.MessageRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val repository: MessageRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        channelTitle: String,
        messageContent: String,
        topicTitle: String,
    ): Int = withContext(dispatcher) {
        val message = OutcomeMessage(
            channelTitle = channelTitle,
            topicTitle = topicTitle,
            content = messageContent,
            type = com.study.network.model.request.message.MessageType.STREAM
        )
        repository.sendMessage(message)
    }
}
