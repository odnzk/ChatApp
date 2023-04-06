package com.study.channels.domain.repository

import com.study.channels.domain.model.Channel
import com.study.channels.domain.model.ChannelTopic

interface ChannelRepository {
    suspend fun getAll(): List<Channel>
    suspend fun getSubscribedStreams(): List<Channel>
    suspend fun getStreamTopics(streamId: Int): List<ChannelTopic>
}
