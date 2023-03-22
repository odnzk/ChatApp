package com.study.tinkoff.feature.users.domain

import com.study.domain.model.User

interface UsersRepository {
    suspend fun getUsers(): List<User>
    suspend fun getUserById(id: Int): User?
    suspend fun getUserByEmail(email: String): User?
}
