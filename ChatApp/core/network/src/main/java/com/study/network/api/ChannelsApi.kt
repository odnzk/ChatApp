package com.study.network.api


import com.study.network.model.request.channel.ChannelRequestDto
import com.study.network.model.response.stream.AddStreamResponse
import com.study.network.model.response.stream.AllStreamsResponse
import com.study.network.model.response.stream.StreamTopicsResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ChannelsApi {

    @GET("users/me/subscriptions")
    suspend fun getSubscribedStreams(): AllStreamsResponse

    @POST("users/me/subscriptions")
    suspend fun createStream(
        @Query("subscriptions")
        channelRequestDto: ChannelRequestDto
    ): AddStreamResponse

    @DELETE("users/me/subscriptions")
    suspend fun unsubscribeFromStream(
        @Query("subscriptions")
        channelRequestDto: String
    )

    @GET("streams")
    suspend fun getAllStreams(
        @Query("include_subscribed") includeSubscribed: Boolean = false
    ): AllStreamsResponse

    @GET("users/me/{stream_id}/topics")
    suspend fun getStreamTopics(@Path("stream_id") streamId: Int): StreamTopicsResponse

}
