package com.study.profile.data.source

import com.study.network.ZulipApi
import com.study.network.model.response.user.DetailedUserDto
import com.study.network.model.response.user.UserPresenceResponse
import com.study.network.model.response.user.UserResponse
import com.study.network.util.BaseNetworkDataSource
import javax.inject.Inject

class RemoteUserDataSource @Inject constructor(private val api: ZulipApi) :
    BaseNetworkDataSource() {
    suspend fun getCurrentUser(): DetailedUserDto = makeNetworkRequest { api.getCurrentUser() }
    suspend fun getUserById(id: Int): UserResponse = makeNetworkRequest { api.getUserById(id) }
    suspend fun getUserPresence(userId: Int): UserPresenceResponse =
        makeNetworkRequest { api.getUserPresence(userId) }
}
