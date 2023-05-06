package com.study.network.dataSource

import com.study.network.ZulipApi
import com.study.network.model.request.message.ChannelRequestDto
import com.study.network.model.response.stream.AddStreamResponse
import com.study.network.model.response.stream.AllStreamsResponse
import com.study.network.model.response.stream.StreamTopicsResponse
import javax.inject.Inject

class ChannelRemoteDataSource @Inject constructor(private val api: ZulipApi) :
    BaseNetworkDataSource() {
    suspend fun getChannels(isSubscribed: Boolean): AllStreamsResponse =
        safeRequest { if (isSubscribed) api.getSubscribedStreams() else api.getAllStreams() }

    suspend fun getChannelTopics(streamId: Int): StreamTopicsResponse =
        safeRequest { api.getStreamTopics(streamId) }

    suspend fun addChannel(title: String, isHistoryPublic: Boolean): AddStreamResponse =
        safeRequest { api.createStream(ChannelRequestDto(title), isHistoryPublic) }

    suspend fun unsubscribeFromChannel(title: String) =
        safeRequest { api.unsubscribeFromStream("[\"$title]\"") }

}
