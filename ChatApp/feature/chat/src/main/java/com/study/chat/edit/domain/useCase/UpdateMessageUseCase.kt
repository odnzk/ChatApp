package com.study.chat.edit.domain.useCase

import com.study.chat.edit.domain.repository.EditMessageRepository
import com.study.chat.shared.domain.model.NOT_YET_SYNCHRONIZED_ID
import com.study.chat.shared.domain.model.OutcomeMessage
import com.study.common.validation.Validator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class UpdateMessageUseCase @Inject constructor(
    private val validator: Validator<OutcomeMessage>,
    private val repository: EditMessageRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(messageId: Int, content: String, topic: String) =
        withContext(dispatcher) {
            validator.validate(
                OutcomeMessage.Text(
                    channelId = NOT_YET_SYNCHRONIZED_ID,
                    senderId = NOT_YET_SYNCHRONIZED_ID,
                    topicTitle = topic,
                    content = content
                )
            )
            repository.updateMessage(messageId, content, topic)
        }
}
