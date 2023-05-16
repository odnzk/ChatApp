package com.study.users.data.source

import com.study.network.ZulipApi
import com.study.network.model.response.user.AllUserPresenceDto
import com.study.network.model.response.user.AllUsersResponse
import com.study.network.util.BaseNetworkDataSource
import javax.inject.Inject

internal class RemoteUserDataSource @Inject constructor(private val api: ZulipApi) :
    BaseNetworkDataSource() {
    suspend fun getAllUsers(): AllUsersResponse = safeRequest { api.getAllUsers() }
    suspend fun getAllUserPresence(): AllUserPresenceDto = safeRequest { api.getAllUserPresence() }
}
