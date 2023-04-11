package com.study.profile.domain.usecase

import com.study.auth.UserNotAuthorizedException
import com.study.components.model.UserPresenceStatus
import com.study.profile.domain.repository.UserRepository
import com.study.ui.NavConstants
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class GetUserPresenceUseCase(
    private val repository: UserRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(userId: Int): UserPresenceStatus = withContext(dispatcher) {
        if (userId == NavConstants.CURRENT_USER_ID_KEY) {
            throw UserNotAuthorizedException("The user's presence cannot be obtained because the user id is invalid")
        }
        repository.getUserPresence(userId)
    }
}

