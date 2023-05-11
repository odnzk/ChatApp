package com.study.chat.domain.usecase

import com.study.chat.domain.model.OutcomeMessage
import com.study.chat.domain.repository.MessageRepository
import com.study.common.Validator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class UpdateMessageUseCase @Inject constructor(
    private val validator: Validator<OutcomeMessage>,
    private val repository: MessageRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(messageId: Int, content: String, topic: String) =
        withContext(dispatcher) {
            validator.validate(OutcomeMessage.defaultOutcomeMessage(content, topic))
            repository.updateMessage(messageId, content, topic)
        }
}
