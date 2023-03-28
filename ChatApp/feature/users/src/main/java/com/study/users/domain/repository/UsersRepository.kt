package com.study.users.domain.repository

import com.study.users.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UsersRepository {
    fun getUsers(): Flow<List<User>>
    fun getUsersByEmail(email: String): List<User>
}
