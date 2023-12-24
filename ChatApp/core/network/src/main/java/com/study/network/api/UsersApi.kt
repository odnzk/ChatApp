package com.study.network.api


import com.study.network.model.request.ChannelRequestDto
import com.study.network.model.request.message.MessageNarrowList
import com.study.network.model.request.message.MessageType
import com.study.network.model.request.message.MessagesAnchor
import com.study.network.model.response.message.AllMessagesResponse
import com.study.network.model.response.message.SentMessageResponse
import com.study.network.model.response.message.UploadedFileResponse
import com.study.network.model.response.stream.AddStreamResponse
import com.study.network.model.response.stream.AllStreamsResponse
import com.study.network.model.response.stream.StreamTopicsResponse
import com.study.network.model.response.user.AllUserPresenceDto
import com.study.network.model.response.user.AllUsersResponse
import com.study.network.model.response.user.DetailedUserDto
import com.study.network.model.response.user.UserPresenceResponse
import com.study.network.model.response.user.UserResponse
import okhttp3.MultipartBody
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

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
