package com.study.chat.shared.data.source.remote

import com.study.network.ZulipApi
import com.study.network.model.response.stream.StreamTopicsResponse
import com.study.network.util.BaseNetworkDataSource
import dagger.Reusable
import javax.inject.Inject

@Reusable
internal class RemoteTopicDataSource @Inject constructor(private val api: ZulipApi) :
    BaseNetworkDataSource() {

    suspend fun getChannelTopics(streamId: Int): StreamTopicsResponse =
        makeNetworkRequest { api.getStreamTopics(streamId) }

}
