package com.study.profile.data

import com.study.components.model.UserPresenceStatus
import com.study.network.impl.ZulipApi
import com.study.profile.data.mapper.toDetailedUser
import com.study.profile.data.mapper.toUserPresenceStatus
import com.study.profile.domain.model.UserDetailed
import com.study.profile.domain.repository.UserRepository

internal class RemoteUserRepository(private val api: ZulipApi) : UserRepository {
    override suspend fun getUserById(id: Int): UserDetailed? {
        return api.getUserById(id).user?.toDetailedUser()
    }

    override suspend fun getCurrentUser(): UserDetailed {
        return api.getCurrentUser().toDetailedUser()
    }

    override suspend fun getUserPresence(userId: Int): UserPresenceStatus {
        return api.getUserPresence(userId).toUserPresenceStatus()
    }

}
