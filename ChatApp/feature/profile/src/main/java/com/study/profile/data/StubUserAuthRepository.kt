package com.study.profile.data

import com.study.profile.domain.model.UserDetailed
import com.study.profile.domain.repository.UserAuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

internal class StubUserAuthRepository : UserAuthRepository {
    private var currentUser: UserDetailed? = UserDetailed(
        0, name = "Darrell Steward", email = "darrel@company.com", avatarUrl = "", isActive = false
    )

    override suspend fun logout() {
        if (Random.nextBoolean()) {
            throw RuntimeException()
        }
        currentUser = null
    }

    override suspend fun getCurrentUser(): Flow<UserDetailed?> = flow {
        if (Random.nextBoolean()) {
            throw RuntimeException()
        }
        delay(2000)
        emit(currentUser)
    }
}
