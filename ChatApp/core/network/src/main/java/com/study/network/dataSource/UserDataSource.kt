package com.study.network.dataSource

import com.study.network.ZulipApi
import com.study.network.model.response.user.AllUserPresenceDto
import com.study.network.model.response.user.AllUsersResponse
import com.study.network.model.response.user.DetailedUserDto
import com.study.network.model.response.user.UserPresenceResponse
import com.study.network.model.response.user.UserResponse

import javax.inject.Inject

class UserDataSource @Inject constructor(private val api: ZulipApi) {

    suspend fun getAllUsers(): AllUsersResponse = api.getAllUsers()

    suspend fun getCurrentUser(): DetailedUserDto = api.getCurrentUser()

    suspend fun getUserById(id: Int): UserResponse = api.getUserById(id)
    suspend fun getAllUserPresence(): AllUserPresenceDto = api.getAllUserPresence()
    suspend fun getUserPresence(userId: Int): UserPresenceResponse =
        api.getUserPresence(userId)
}
