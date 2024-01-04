package com.study.chat.actions.domain.usecase

import com.study.chat.actions.domain.repository.ActionsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class CopyMessageUseCase @Inject constructor(
    private val repository: ActionsRepository,
    private val dispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(messageId: Int): Unit = withContext(dispatcher) {
        repository.copyMessage(messageId)
    }
}

