package com.odnzk.auth.domain.usecase

import com.odnzk.auth.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(email: String, password: String) = withContext(dispatcher) {
        authRepository.login(email = email, password = password)
    }
}