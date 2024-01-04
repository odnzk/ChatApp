package com.odnzk.auth.domain.usecase

import com.odnzk.auth.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class LoginUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String) = withContext(Dispatchers.IO) {
        authRepository.login(email = email, password = password)
    }
}