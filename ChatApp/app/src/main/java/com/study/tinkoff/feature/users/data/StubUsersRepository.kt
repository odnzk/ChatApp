package com.study.tinkoff.feature.users.data

import com.study.domain.model.User
import com.study.tinkoff.feature.users.domain.UsersRepository

class StubUsersRepository : UsersRepository {
    private val generator = RandomUsersGenerator()
    private val users = generator.generateRandomUsers(USER_COUNT)

    override suspend fun getUsers(): List<User> {
        return users
    }

    override suspend fun getUserById(id: Int): User? {
        return users.find { it.id == id }
    }

    override suspend fun getUserByEmail(email: String): User? {
        return users.find { it.email == email }
    }

    companion object {
        private const val USER_COUNT = 10
    }
}
