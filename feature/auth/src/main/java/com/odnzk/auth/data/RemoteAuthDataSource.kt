package com.odnzk.auth.data

import com.study.network.api.AuthApi
import com.study.network.model.request.user.CreateUserRequest
import com.study.network.model.response.user.ApiKeyResponse
import com.study.network.util.BaseNetworkDataSource
import dagger.Reusable
import javax.inject.Inject

@Reusable
internal class RemoteAuthDataSource @Inject constructor(private val api: AuthApi) :
    BaseNetworkDataSource() {
    suspend fun login(email: String, password: String): ApiKeyResponse {
        return safeNetworkRequest { api.fetchApiKey(email, password) }
    }

    suspend fun signup(email: String, password: String, fullName: String) {
        return safeNetworkRequest {
            api.createUser(
                CreateUserRequest(
                    email = email,
                    password = password,
                    fullName = fullName
                )
            )
        }
    }
}