package com.study.network

class StubUserRepository {
    private val api = NetworkModule.providesApi()

    suspend fun getCurrentUserId(): Int {
        return requireNotNull(api.getCurrentUser().userId)
    }
}
