package com.study.profile.domain.repository

import com.study.profile.domain.model.UserDetailed
import com.study.profile.domain.model.UserPresence

internal interface UserRepository {
    suspend fun getUserById(id: Int): UserDetailed?
    suspend fun getCurrentUser(): UserDetailed
    suspend fun getUserPresence(userId: Int): UserPresence
}
