package com.study.auth.api

interface Authentificator {
    suspend fun getEmail(): String
    suspend fun getApiKey(): String

    suspend fun saveEmail(email: String)
    suspend fun saveUserId(userId: Int)
    suspend fun saveApiKey(apiKey: String)
    suspend fun getUserId(): Int
    suspend fun clear()
    suspend fun isAuthorized(): Boolean
}
