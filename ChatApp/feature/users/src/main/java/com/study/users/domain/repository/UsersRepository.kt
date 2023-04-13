package com.study.users.domain.repository

import com.study.network.impl.ZulipApi
import com.study.users.data.RemoteUserRepository
import com.study.users.domain.model.User
import com.study.users.domain.model.UserPresence

interface UsersRepository {
    suspend fun getUsers(): List<User>

    suspend fun getUsersPresence(): List<UserPresence>

    companion object {
        operator fun invoke(api: ZulipApi): UsersRepository = RemoteUserRepository(api)
    }
}
