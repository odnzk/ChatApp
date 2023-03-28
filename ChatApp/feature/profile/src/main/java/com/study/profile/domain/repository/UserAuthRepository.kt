package com.study.profile.domain.repository

import com.study.profile.domain.model.UserDetailed
import kotlinx.coroutines.flow.Flow

interface UserAuthRepository {
    suspend fun logout()
    suspend fun getCurrentUser(): Flow<UserDetailed?>
}
