package com.study.network.dataSource

import com.study.network.ZulipApi
import com.study.network.model.response.user.AllUserPresenceDto
import com.study.network.model.response.user.AllUsersResponse
import com.study.network.model.response.user.DetailedUserDto
import com.study.network.model.response.user.UserPresenceResponse
import com.study.network.model.response.user.UserResponse

import javax.inject.Inject

class UserDataSource @Inject constructor(private val api: ZulipApi) : BaseNetworkDataSource() {

    suspend fun getAllUsers(): AllUsersResponse = safeRequest { api.getAllUsers() }

    suspend fun getCurrentUser(): DetailedUserDto = safeRequest { api.getCurrentUser() }

    suspend fun getUserById(id: Int): UserResponse = safeRequest { api.getUserById(id) }
    suspend fun getAllUserPresence(): AllUserPresenceDto = safeRequest { api.getAllUserPresence() }
    suspend fun getUserPresence(userId: Int): UserPresenceResponse =
        safeRequest { api.getUserPresence(userId) }
}
