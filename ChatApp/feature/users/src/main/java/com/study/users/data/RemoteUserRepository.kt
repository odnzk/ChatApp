package com.study.users.data

import com.study.network.impl.ZulipApi
import com.study.users.data.mapper.toUserList
import com.study.users.data.mapper.toUserPresenceList
import com.study.users.domain.model.User
import com.study.users.domain.model.UserPresence
import com.study.users.domain.repository.UsersRepository

internal class RemoteUserRepository(private val api: ZulipApi) : UsersRepository {
    override suspend fun getUsers(): List<User> {
        return api.getAllUsers().toUserList()
    }

    override suspend fun getUsersPresence(): List<UserPresence> {
        return api.getAllUserPresence().toUserPresenceList()
    }

}
