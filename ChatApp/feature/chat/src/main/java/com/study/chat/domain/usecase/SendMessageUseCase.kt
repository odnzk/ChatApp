package com.study.chat.domain.usecase

import com.study.chat.domain.model.OutcomeMessage
import com.study.chat.domain.repository.MessageRepository
import com.study.common.Validator
import com.study.network.model.request.message.MessageType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.Calendar
import javax.inject.Inject

internal class SendMessageUseCase @Inject constructor(
    private val validator: Validator<OutcomeMessage>,
    private val repository: MessageRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        channelId: Int,
        messageContent: String,
        topicTitle: String,
        senderId: Int
    ) = withContext(dispatcher) {
        val message = OutcomeMessage(
            channelId = channelId,
            topicTitle = topicTitle,
            content = messageContent,
            type = MessageType.STREAM,
            calendar = Calendar.getInstance(),
            senderId = senderId
        )
        validator.validate(message)
        repository.sendMessage(message)
    }
}
