package com.study.tinkoff.feature.profile.data

import com.study.domain.model.User
import com.study.tinkoff.feature.profile.domain.UserAuthRepository

class StubUserAuthRepository : UserAuthRepository {
    private var currentUser: User? =
        User(
            0,
            name = "Darrell Steward",
            email = "darrel@company.com",
            avatarUrl = "",
            is_active = false
        )

    override suspend fun logout() {
        currentUser = null
    }

    override suspend fun getCurrentUser(): User? {
        return currentUser
    }

}
