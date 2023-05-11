package com.study.users.domain.repository

import com.study.users.domain.model.User
import com.study.users.domain.model.UserPresence

internal interface UsersRepository {
    suspend fun getUsers(): List<User>

    suspend fun getUsersPresence(): List<UserPresence>

}
