package com.study.chat.chat.domain.usecase

import com.study.auth.api.Authentificator
import com.study.chat.chat.domain.repository.ChatRepository
import com.study.chat.common.domain.model.OutcomeMessage
import com.study.common.validation.Validator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class UploadFileUseCase @Inject constructor(
    private val authentificator: Authentificator,
    private val validator: Validator<OutcomeMessage>,
    private val repository: ChatRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(channelId: Int, topicTitle: String, uri: String) =
        withContext(dispatcher) {
            val message = OutcomeMessage.File(
                channelId = channelId,
                topicTitle = topicTitle,
                uri = uri,
                senderId = authentificator.getUserId()
            )
            validator.validate(message)
            repository.sendMessage(message)
        }
}
