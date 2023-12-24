package com.study.users.data.source

import com.study.network.api.UsersApi
import com.study.network.model.response.user.AllUserPresenceDto
import com.study.network.model.response.user.AllUsersResponse
import com.study.network.util.BaseNetworkDataSource
import javax.inject.Inject

internal class RemoteUserDataSource @Inject constructor(private val api: UsersApi) :
    BaseNetworkDataSource() {
    suspend fun getAllUsers(): AllUsersResponse = makeNetworkRequest { api.getAllUsers() }
    suspend fun getAllUserPresence(): AllUserPresenceDto = makeNetworkRequest { api.getAllUserPresence() }
}
