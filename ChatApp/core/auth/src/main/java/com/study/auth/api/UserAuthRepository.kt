package com.study.auth.api

interface UserAuthRepository {
    suspend fun getUserId(): Int
    suspend fun isAdmin(): Boolean
}
