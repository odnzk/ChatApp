package com.study.chat.domain.usecase

import com.study.auth.UserAuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetCurrentUserIdUseCase(
    private val repository: UserAuthRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(): Int = withContext(dispatcher) { repository.getCurrentUserId() }
}
