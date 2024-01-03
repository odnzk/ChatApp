package com.odnzk.auth.domain.usecase

import com.odnzk.auth.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class SignupUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String, fullName: String) =
        withContext(Dispatchers.IO) {
            authRepository.signup(email = email, password = password, fullName = fullName)
        }
}