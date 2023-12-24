package com.study.network.api


import com.study.network.model.response.user.DetailedUserDto
import retrofit2.http.GET

interface AuthApi {

    @GET("users/me")
    suspend fun getCurrentUser(): DetailedUserDto

}
