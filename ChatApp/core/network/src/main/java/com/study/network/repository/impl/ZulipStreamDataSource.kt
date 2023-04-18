package com.study.network.repository.impl

import com.study.network.ZulipApi
import com.study.network.model.response.stream.AllStreamsResponse
import com.study.network.model.response.stream.StreamDetailedDto
import com.study.network.model.response.stream.StreamTopicsResponse
import com.study.network.model.response.stream.SubscribedStreamsResponse

import com.study.network.repository.StreamDataSource
import javax.inject.Inject

internal class ZulipStreamDataSource @Inject constructor(private val api: ZulipApi) :
    StreamDataSource {

    override suspend fun getSubscribedStreams(areSubscribedUsersIncluded: Boolean): SubscribedStreamsResponse =
        api.getSubscribedStreams(areSubscribedUsersIncluded)

    override suspend fun getAllStreams(
        arePublicIncluded: Boolean,
        areSubscribedIncluded: Boolean
    ): AllStreamsResponse = api.getAllStreams(arePublicIncluded, areSubscribedIncluded)

    override suspend fun getStreamById(streamId: Int): StreamDetailedDto =
        api.getStreamById(streamId)

    override suspend fun getStreamTopics(streamId: Int): StreamTopicsResponse =
        api.getStreamTopics(streamId)
}
