package com.study.chat.common.data.source.remote

import com.study.network.api.ChannelsApi
import com.study.network.model.response.stream.StreamTopicsResponse
import com.study.network.util.BaseNetworkDataSource
import dagger.Reusable
import javax.inject.Inject

@Reusable
internal class RemoteTopicDataSource @Inject constructor(private val api: ChannelsApi) :
    BaseNetworkDataSource() {

    suspend fun getChannelTopics(streamId: Int): StreamTopicsResponse =
        makeNetworkRequest { api.getStreamTopics(streamId) }

}
