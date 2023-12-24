package com.study.chat.common.domain.usecase

import com.study.auth.api.UserAuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class GetCurrentUserIdUseCase @Inject constructor(
    private val repository: UserAuthRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(): Int = withContext(dispatcher) {
        repository.getUserId()
    }
}
