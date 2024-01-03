package com.odnzk.auth.data

import com.odnzk.auth.domain.repository.AuthRepository
import com.study.auth.api.Authentificator
import dagger.Reusable
import javax.inject.Inject

@Reusable
internal class AuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteAuthDataSource,
    private val authentificator: Authentificator
) : AuthRepository {
    override suspend fun login(username: String, password: String) {
        val user = remoteDataSource.login(username, password)
        authentificator.saveUserId(user.userId)
        authentificator.saveApiKey(user.apiKey)
    }

    override suspend fun signup(email: String, password: String, fullName: String) {
        remoteDataSource.signup(email, password, fullName)
    }
}