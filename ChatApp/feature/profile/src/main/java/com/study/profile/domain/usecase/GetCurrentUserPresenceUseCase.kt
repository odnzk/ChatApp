package com.study.profile.domain.usecase

import com.study.auth.api.UserAuthRepository
import com.study.profile.domain.model.UserPresence
import com.study.profile.domain.repository.UserRepository
import dagger.Reusable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

@Reusable
internal class GetCurrentUserPresenceUseCase @Inject constructor(
    private val userAuthRepository: UserAuthRepository,
    private val repository: UserRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(): UserPresence = withContext(dispatcher) {
        repository.getUserPresence(userAuthRepository.getUserId())
    }
}

