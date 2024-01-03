package com.odnzk.auth.domain.repository

internal interface AuthRepository {
    suspend fun login(username: String, password: String)
    suspend fun signup(email: String, password: String, fullName: String)
}