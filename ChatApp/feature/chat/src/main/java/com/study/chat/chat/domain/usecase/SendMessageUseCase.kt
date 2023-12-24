package com.study.chat.chat.domain.usecase

import com.study.chat.chat.domain.repository.ChatRepository
import com.study.chat.common.domain.model.OutcomeMessage
import com.study.common.validation.Validator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class SendMessageUseCase @Inject constructor(
    private val validator: Validator<OutcomeMessage>,
    private val repository: ChatRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        channelId: Int,
        content: String,
        topicTitle: String,
        senderId: Int
    ) = withContext(dispatcher) {
        val message = OutcomeMessage.Text(
            channelId = channelId,
            topicTitle = topicTitle,
            content = content,
            senderId = senderId,
        )
        validator.validate(message)
        repository.sendMessage(message)
    }
}
