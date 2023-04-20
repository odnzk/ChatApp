package com.study.network.repository.impl

import com.study.network.ZulipApi
import com.study.network.model.response.user.AllUserPresenceDto
import com.study.network.model.response.user.AllUsersResponse
import com.study.network.model.response.user.DetailedUserDto
import com.study.network.model.response.user.UserPresenceResponse
import com.study.network.model.response.user.UserResponse

import com.study.network.repository.UserDataSource
import javax.inject.Inject

internal class ZulipUserDataSource @Inject constructor(private val api: ZulipApi) :
    UserDataSource {

    override suspend fun getAllUsers(): AllUsersResponse = api.getAllUsers()

    override suspend fun getCurrentUser(): DetailedUserDto = api.getCurrentUser()

    override suspend fun getUserById(id: Int): UserResponse = api.getUserById(id)

    override suspend fun getAllUserPresence(): AllUserPresenceDto = api.getAllUserPresence()

    override suspend fun getUserPresence(userId: Int): UserPresenceResponse =
        api.getUserPresence(userId)
}
