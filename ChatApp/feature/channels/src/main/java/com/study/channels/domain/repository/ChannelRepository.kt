package com.study.channels.domain.repository

import com.study.channels.domain.model.Channel
import com.study.channels.domain.model.ChannelTopic
import kotlinx.coroutines.flow.Flow

internal interface ChannelRepository {
    fun getChannels(isSubscribed: Boolean): Flow<List<Channel>>
    fun getChannelTopics(channelId: Int): Flow<List<ChannelTopic>>
    suspend fun loadChannels(isSubscribed: Boolean)
    suspend fun loadChannelTopics(channelId: Int)
    suspend fun addChannel(channel: Channel)
}

