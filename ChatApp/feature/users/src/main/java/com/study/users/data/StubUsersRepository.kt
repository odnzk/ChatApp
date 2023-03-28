package com.study.users.data

import com.study.users.domain.model.User
import com.study.users.domain.repository.UsersRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class StubUsersRepository : UsersRepository {
    private val generator = RandomUsersGenerator()
    private val users = generator.generateRandomUsers(USER_COUNT)

    override fun getUsers(): Flow<List<User>> = flow {
        delay(2000)
        emit(users)
    }

    override fun getUsersByEmail(email: String): List<User> {
        return users.filter { it.email.startsWith(email, ignoreCase = true) }
    }

    companion object {
        private const val USER_COUNT = 10
    }
}
