package com.study.profile.data.source

import com.study.network.api.UsersApi
import com.study.network.model.response.user.DetailedUserDto
import com.study.network.model.response.user.UserPresenceResponse
import com.study.network.model.response.user.UserResponse
import com.study.network.util.BaseNetworkDataSource
import javax.inject.Inject

class RemoteUserDataSource @Inject constructor(private val api: UsersApi) :
    BaseNetworkDataSource() {
    suspend fun getCurrentUser(): DetailedUserDto = safeNetworkRequest { api.getCurrentUser() }
    suspend fun getUserById(id: Int): UserResponse = safeNetworkRequest { api.getUserById(id) }
    suspend fun getUserPresence(userId: Int): UserPresenceResponse =
        safeNetworkRequest { api.getUserPresence(userId) }
}
