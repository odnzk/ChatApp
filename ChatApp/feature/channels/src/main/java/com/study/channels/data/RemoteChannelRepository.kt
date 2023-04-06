package com.study.channels.data

import com.study.channels.data.mapper.toChannelList
import com.study.channels.data.mapper.toChannelTopicList
import com.study.channels.domain.model.Channel
import com.study.channels.domain.model.ChannelTopic
import com.study.channels.domain.repository.ChannelRepository
import com.study.network.NetworkModule

internal class RemoteChannelRepository : ChannelRepository {
    private val api = NetworkModule.providesApi()

    override suspend fun getAll(): List<Channel> {
        return api.getAllStreams().toChannelList()
    }

    override suspend fun getSubscribedStreams(): List<Channel> {
        return api.getSubscribedStreams().toChannelList()
    }

    override suspend fun getStreamTopics(streamId: Int): List<ChannelTopic> {
        return api.getStreamTopics(streamId).toChannelTopicList()
    }
}
