package com.study.network.api


import com.study.network.model.request.message.MessageNarrowList
import com.study.network.model.request.message.MessageType
import com.study.network.model.request.message.MessagesAnchor
import com.study.network.model.response.message.AllMessagesResponse
import com.study.network.model.response.message.SentMessageResponse
import com.study.network.model.response.message.UploadedFileResponse
import okhttp3.MultipartBody
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface MessagesApi {

    @POST("messages")
    suspend fun sendMessage(
        @Query("type") type: MessageType,
        @Query("to") to: Int,
        @Query("content") content: String,
        @Query("topic") topic: String?
    ): SentMessageResponse

    @GET("messages")
    suspend fun getMessages(
        @Query("num_before") numBefore: Int,
        @Query("num_after") numAfter: Int,
        @Query("anchor") anchor: MessagesAnchor,
        @Query("narrow") narrow: MessageNarrowList = MessageNarrowList(),
        @Query("apply_markdown") applyMarkdown: Boolean = false,
    ): AllMessagesResponse

    @GET("messages")
    suspend fun getMessages(
        @Query("num_before") numBefore: Int,
        @Query("num_after") numAfter: Int,
        @Query("anchor") anchorMessageId: Int,
        @Query("narrow") narrow: MessageNarrowList = MessageNarrowList(),
        @Query("apply_markdown") applyMarkdown: Boolean = false,
        @Query("include_anchor") includeAnchor: Boolean = false
    ): AllMessagesResponse


    @DELETE("messages/{message_id}")
    suspend fun deleteMessage(@Path("message_id") messageId: Int)

    @PATCH("messages/{message_id}")
    suspend fun updateMessage(
        @Path("message_id") messageId: Int,
        @Query("topic") topic: String,
        @Query("content") content: String
    )

    @POST("user_uploads")
    @Multipart
    suspend fun upload(@Part body: MultipartBody.Part): UploadedFileResponse

    @POST("messages/{message_id}/reactions")
    suspend fun addReactionToMessage(
        @Path("message_id") mesId: Int,
        @Query("emoji_name") emojiName: String
    )

    @DELETE("messages/{message_id}/reactions")
    suspend fun removeReactionFromMessage(
        @Path("message_id") mesId: Int,
        @Query("emoji_name") emojiName: String
    )

}
