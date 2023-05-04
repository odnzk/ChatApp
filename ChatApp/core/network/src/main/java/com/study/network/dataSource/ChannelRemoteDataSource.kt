package com.study.network.dataSource

import com.study.network.ZulipApi
import com.study.network.model.response.stream.AllStreamsResponse
import com.study.network.model.response.stream.StreamDetailedDto
import com.study.network.model.response.stream.StreamTopicsResponse
import javax.inject.Inject

class ChannelRemoteDataSource @Inject constructor(private val api: ZulipApi) :
    BaseNetworkDataSource() {
    suspend fun getChannels(isSubscribed: Boolean): AllStreamsResponse =
        safeRequest { if (isSubscribed) api.getSubscribedStreams() else api.getAllStreams() }

    suspend fun getChannelById(streamId: Int): StreamDetailedDto =
        safeRequest { api.getStreamById(streamId) }

    suspend fun getChannelTopics(streamId: Int): StreamTopicsResponse =
        safeRequest { api.getStreamTopics(streamId) }
}
