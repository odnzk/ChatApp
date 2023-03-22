package com.study.tinkoff.feature.channels.domain

import com.study.domain.model.Channel
import com.study.domain.model.ChannelTopic

interface StreamRepository {
    suspend fun getAll(): List<Channel>
    suspend fun getSubscribedStreams(): List<Channel>
    suspend fun getStreamById(id: Int): Channel?
    suspend fun getStreamTopics(streamId: Int): List<ChannelTopic>
}
