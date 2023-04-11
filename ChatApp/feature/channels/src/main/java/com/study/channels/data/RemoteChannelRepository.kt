package com.study.channels.data

import com.study.channels.data.mapper.toChannelList
import com.study.channels.data.mapper.toChannelTopicList
import com.study.channels.domain.model.Channel
import com.study.channels.domain.model.ChannelTopic
import com.study.channels.domain.repository.ChannelRepository
import com.study.network.impl.ZulipApi

internal class RemoteChannelRepository(private val api: ZulipApi) : ChannelRepository {
    override suspend fun getAllChannels(): List<Channel> {
        return api.getAllStreams().toChannelList()
    }

    override suspend fun getSubscribedChannels(): List<Channel> {
        return api.getSubscribedStreams().toChannelList()
    }

    override suspend fun getChannelTopics(streamId: Int): List<ChannelTopic> {
        return api.getStreamTopics(streamId).toChannelTopicList()
    }
}
