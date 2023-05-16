package com.study.auth.impl

import com.study.network.ZulipApi
import com.study.network.model.response.user.DetailedUserDto
import com.study.network.util.BaseNetworkDataSource
import dagger.Reusable
import javax.inject.Inject

@Reusable
internal class RemoteUserDataSource @Inject constructor(private val api: ZulipApi) :
    BaseNetworkDataSource() {
    suspend fun getCurrentUser(): DetailedUserDto = safeRequest { api.getCurrentUser() }
}
