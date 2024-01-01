package com.study.channels.common.data.source

import com.study.network.api.ChannelsApi
import com.study.network.model.request.ChannelRequestDto
import com.study.network.model.response.stream.AddStreamResponse
import com.study.network.model.response.stream.AllStreamsResponse
import com.study.network.model.response.stream.StreamTopicsResponse
import com.study.network.util.BaseNetworkDataSource
import dagger.Reusable
import javax.inject.Inject

@Reusable
internal class RemoteChannelDataSource @Inject constructor(private val api: ChannelsApi) :
    BaseNetworkDataSource() {
    suspend fun getChannels(isSubscribed: Boolean): AllStreamsResponse =
        safeNetworkRequest { if (isSubscribed) api.getSubscribedStreams() else api.getAllStreams() }

    suspend fun getChannelTopics(streamId: Int): StreamTopicsResponse =
        safeNetworkRequest { api.getStreamTopics(streamId) }

    suspend fun addChannel(title: String): AddStreamResponse =
        safeNetworkRequest { api.createStream(ChannelRequestDto(title)) }

    suspend fun unsubscribeFromChannel(title: String) =
        safeNetworkRequest { api.unsubscribeFromStream("[\"$title]\"") }
}
