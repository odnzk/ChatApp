package com.study.channels.domain.repository

import com.study.channels.domain.model.Channel
import com.study.channels.domain.model.ChannelFilter
import com.study.channels.domain.model.ChannelTopic
import kotlinx.coroutines.flow.Flow

interface ChannelRepository {
    fun getChannels(channelFilter: ChannelFilter): Flow<List<Channel>>
    suspend fun getChannelTopics(channelId: Int): List<ChannelTopic>
    suspend fun loadChannels(channelFilter: ChannelFilter)

    suspend fun loadChannelTopics(channelId: Int)


}

