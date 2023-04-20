package com.study.channels.domain.repository

import com.study.channels.domain.model.Channel
import com.study.channels.domain.model.ChannelTopic

interface ChannelRepository {
    suspend fun getAllChannels(): List<Channel>
    suspend fun getSubscribedChannels(): List<Channel>
    suspend fun getChannelTopics(streamId: Int): List<ChannelTopic>

}

