package com.study.channels.shared.data.source

import com.study.network.ZulipApi
import com.study.network.model.request.ChannelRequestDto
import com.study.network.model.response.stream.AddStreamResponse
import com.study.network.model.response.stream.AllStreamsResponse
import com.study.network.model.response.stream.StreamTopicsResponse
import com.study.network.util.BaseNetworkDataSource
import dagger.Reusable
import javax.inject.Inject

@Reusable
internal class RemoteChannelDataSource @Inject constructor(private val api: ZulipApi) :
    BaseNetworkDataSource() {
    suspend fun getChannels(isSubscribed: Boolean): AllStreamsResponse =
        makeNetworkRequest { if (isSubscribed) api.getSubscribedStreams() else api.getAllStreams() }

    suspend fun getChannelTopics(streamId: Int): StreamTopicsResponse =
        makeNetworkRequest { api.getStreamTopics(streamId) }

    suspend fun addChannel(title: String): AddStreamResponse =
        makeNetworkRequest { api.createStream(ChannelRequestDto(title)) }

    suspend fun unsubscribeFromChannel(title: String) =
        makeNetworkRequest { api.unsubscribeFromStream("[\"$title]\"") }
}
