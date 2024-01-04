package com.study.network.api


import com.study.network.model.response.user.AllUserPresenceDto
import com.study.network.model.response.user.AllUsersResponse
import com.study.network.model.response.user.DetailedUserDto
import com.study.network.model.response.user.UserPresenceResponse
import com.study.network.model.response.user.UserResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface UsersApi {
    @GET("users")
    suspend fun getAllUsers(): AllUsersResponse

    @GET("users/me")
    suspend fun getCurrentUser(): DetailedUserDto

    @GET("users/{user_id}")
    suspend fun getUserById(@Path("user_id") id: Int): UserResponse

    @GET("realm/presence")
    suspend fun getAllUserPresence(): AllUserPresenceDto

    @GET("users/{user_id}/presence")
    suspend fun getUserPresence(@Path("user_id") userId: Int): UserPresenceResponse

}
