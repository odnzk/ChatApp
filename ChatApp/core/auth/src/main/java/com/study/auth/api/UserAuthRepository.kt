package com.study.auth.api

interface UserAuthRepository {
    suspend fun getCurrentUserId(): Int
}
