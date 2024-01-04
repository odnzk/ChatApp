package com.study.network.api


import com.study.network.model.request.user.CreateUserRequest
import com.study.network.model.response.user.ApiKeyResponse
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthApi {

    @POST("fetch_api_key")
    @FormUrlEncoded
    suspend fun fetchApiKey(
        @Field("username") username: String,
        @Field("password") password: String
    ): ApiKeyResponse

    @POST("users")
    suspend fun createUser(@Body request: CreateUserRequest)

}
