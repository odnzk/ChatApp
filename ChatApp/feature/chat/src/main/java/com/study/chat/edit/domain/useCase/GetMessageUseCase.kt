package com.study.chat.edit.domain.useCase

import com.study.chat.edit.domain.repository.EditMessageRepository
import com.study.chat.shared.domain.model.IncomeMessage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class GetMessageUseCase @Inject constructor(
    private val repository: EditMessageRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(messageId: Int): IncomeMessage = withContext(dispatcher) {
        repository.getMessageById(messageId)
    }
}