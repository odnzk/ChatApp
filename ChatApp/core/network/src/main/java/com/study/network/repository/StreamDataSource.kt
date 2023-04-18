package com.study.network.repository

import com.study.network.model.response.stream.AllStreamsResponse
import com.study.network.model.response.stream.StreamDetailedDto
import com.study.network.model.response.stream.StreamTopicsResponse
import com.study.network.model.response.stream.SubscribedStreamsResponse

interface StreamDataSource {

    suspend fun getSubscribedStreams(
        areSubscribedUsersIncluded: Boolean = true
    ): SubscribedStreamsResponse

    suspend fun getAllStreams(
        arePublicIncluded: Boolean = true,
        areSubscribedIncluded: Boolean = true
    ): AllStreamsResponse


    suspend fun getStreamById(streamId: Int): StreamDetailedDto

    suspend fun getStreamTopics(streamId: Int): StreamTopicsResponse


}
