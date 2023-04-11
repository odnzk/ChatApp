package com.study.channels.domain.repository

import com.study.channels.data.RemoteChannelRepository
import com.study.channels.domain.model.Channel
import com.study.channels.domain.model.ChannelTopic
import com.study.network.impl.ZulipApi

interface ChannelRepository {
    suspend fun getAllChannels(): List<Channel>
    suspend fun getSubscribedChannels(): List<Channel>
    suspend fun getChannelTopics(streamId: Int): List<ChannelTopic>

    companion object {
        operator fun invoke(api: ZulipApi): ChannelRepository = RemoteChannelRepository(api)
    }
}

