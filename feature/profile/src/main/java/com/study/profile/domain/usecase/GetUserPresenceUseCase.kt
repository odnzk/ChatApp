package com.study.profile.domain.usecase

import com.study.profile.domain.model.UserPresence
import com.study.profile.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class GetUserPresenceUseCase @Inject constructor(
    private val repository: UserRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(userId: Int): UserPresence = withContext(dispatcher) {
        repository.getUserPresence(userId)
    }
}

