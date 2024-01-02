package com.study.auth.api

interface Authentificator {
    suspend fun getUsername(): String
    suspend fun getApiKey(): String
    suspend fun saveUserId(userId: Int)

    suspend fun saveApiKey(apiKey: String)
    suspend fun getUserId(): Int
    suspend fun clear()
}
