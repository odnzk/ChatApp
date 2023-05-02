package com.study.network


import com.study.network.model.request.message.MessageNarrowList
import com.study.network.model.request.message.MessageType
import com.study.network.model.request.message.MessagesAnchor
import com.study.network.model.response.message.AllMessagesResponse
import com.study.network.model.response.message.SentMessageResponse
import com.study.network.model.response.stream.AllStreamsResponse
import com.study.network.model.response.stream.StreamDetailedDto
import com.study.network.model.response.stream.StreamTopicsResponse
import com.study.network.model.response.user.AllUserPresenceDto
import com.study.network.model.response.user.AllUsersResponse
import com.study.network.model.response.user.DetailedUserDto
import com.study.network.model.response.user.UserPresenceResponse
import com.study.network.model.response.user.UserResponse
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ZulipApi {

    @POST(MESSAGES_END_POINT)
    @FormUrlEncoded
    suspend fun sendMessage(
        @Field("type") type: MessageType,
        @Field("to") to: String,
        @Field("content") content: String,
        @Field("topic") topic: String?
    ): SentMessageResponse

    @GET(MESSAGES_END_POINT)
    suspend fun getMessages(
        @Query("num_before") numBefore: Int,
        @Query("num_after") numAfter: Int,
        @Query("anchor") anchor: MessagesAnchor,
        @Query("narrow") narrow: MessageNarrowList = MessageNarrowList(),
        @Query("apply_markdown") applyMarkdown: Boolean = false,
    ): AllMessagesResponse

    @GET(MESSAGES_END_POINT)
    suspend fun getMessages(
        @Query("num_before") numBefore: Int,
        @Query("num_after") numAfter: Int,
        @Query("anchor") anchorMessageId: Int,
        @Query("narrow") narrow: MessageNarrowList = MessageNarrowList(),
        @Query("apply_markdown") applyMarkdown: Boolean = false,
        @Query("include_anchor") includeAnchor: Boolean = false
    ): AllMessagesResponse

    @POST(UPDATE_REACTION_END_POINT)
    @FormUrlEncoded
    suspend fun addReactionToMessage(
        @Path("message_id") mesId: Int,
        @Field("emoji_name") emojiName: String
    )

    @DELETE(UPDATE_REACTION_END_POINT)
    suspend fun removeReactionFromMessage(
        @Path("message_id") mesId: Int,
        @Query("emoji_name") emojiName: String
    )

    @GET("users/me/subscriptions")
    suspend fun getSubscribedStreams(): AllStreamsResponse

    @GET("streams")
    suspend fun getAllStreams(
        @Query("include_subscribed") includeSubscribed: Boolean = false
    ): AllStreamsResponse

    @GET("streams/{stream_id}")
    suspend fun getStreamById(@Path("stream_id") streamId: Int): StreamDetailedDto

    @GET("users/me/{stream_id}/topics")
    suspend fun getStreamTopics(@Path("stream_id") streamId: Int): StreamTopicsResponse

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

    companion object {
        const val MESSAGES_END_POINT = "messages"
        const val UPDATE_REACTION_END_POINT = "messages/{message_id}/reactions"
    }

}
