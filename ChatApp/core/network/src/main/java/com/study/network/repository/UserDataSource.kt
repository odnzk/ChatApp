package com.study.network.repository

import com.study.network.model.response.user.AllUserPresenceDto
import com.study.network.model.response.user.AllUsersResponse
import com.study.network.model.response.user.DetailedUserDto
import com.study.network.model.response.user.UserPresenceResponse
import com.study.network.model.response.user.UserResponse

interface UserDataSource {
    suspend fun getAllUsers(): AllUsersResponse

    suspend fun getCurrentUser(): DetailedUserDto

    suspend fun getUserById(id: Int): UserResponse

    suspend fun getAllUserPresence(): AllUserPresenceDto

    suspend fun getUserPresence(userId: Int): UserPresenceResponse
}
