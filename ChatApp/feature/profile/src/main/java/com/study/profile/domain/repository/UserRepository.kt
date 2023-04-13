package com.study.profile.domain.repository

import com.study.components.model.UserPresenceStatus
import com.study.network.impl.ZulipApi
import com.study.profile.data.RemoteUserRepository
import com.study.profile.domain.model.UserDetailed

interface UserRepository {
    suspend fun getUserById(id: Int): UserDetailed?

    suspend fun getCurrentUser(): UserDetailed

    suspend fun getUserPresence(userId: Int): UserPresenceStatus

    companion object {
        operator fun invoke(api: ZulipApi): UserRepository = RemoteUserRepository(api)
    }
}
