package com.study.tinkoff.feature.profile.domain

import com.study.domain.model.User

interface UserAuthRepository {
    suspend fun logout()
    suspend fun getCurrentUser(): User?
}
