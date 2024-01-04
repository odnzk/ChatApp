package com.study.users.data

import com.study.users.data.mapper.toUserList
import com.study.users.data.mapper.toUserPresenceList
import com.study.users.data.source.RemoteUserDataSource
import com.study.users.domain.model.User
import com.study.users.domain.model.UserPresence
import com.study.users.domain.repository.UsersRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val dataSource: RemoteUserDataSource,
    private val dispatcher: CoroutineDispatcher
) : UsersRepository {
    override suspend fun getUsers(): List<User> = withContext(dispatcher) {
        dataSource.getAllUsers().toUserList()
    }

    override suspend fun getUsersPresence(): List<UserPresence> = withContext(dispatcher) {
        dataSource.getAllUserPresence().toUserPresenceList()
    }

}
