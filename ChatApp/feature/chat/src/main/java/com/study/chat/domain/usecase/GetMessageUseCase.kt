package com.study.chat.domain.usecase

import com.study.chat.domain.model.IncomeMessage
import com.study.chat.domain.repository.MessageRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class GetMessageUseCase @Inject constructor(
    private val repository: MessageRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(messageId: Int): IncomeMessage = withContext(dispatcher) {
        repository.getMessageById(messageId)
    }
}
