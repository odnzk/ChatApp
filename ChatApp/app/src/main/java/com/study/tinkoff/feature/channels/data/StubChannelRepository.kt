package com.study.tinkoff.feature.channels.data

import com.study.domain.model.Channel
import com.study.domain.model.ChannelTopic
import com.study.tinkoff.feature.channels.domain.StreamRepository

class StubChannelRepository : StreamRepository {
    private val channels = RandomChannelGenerator().generate(5)
    private val topics = listOf("Testing", "Android", "Backend", "Frontend", "Mobile", "UI", "UX")

    override suspend fun getAll(): List<Channel> {
        return channels
    }

    override suspend fun getSubscribedStreams(): List<Channel> {
        return channels.subList(1, 3)
    }

    override suspend fun getStreamById(id: Int): Channel {
        return channels.first { it.id == id }
    }

    override suspend fun getStreamTopics(streamId: Int): List<ChannelTopic> {
        val resList = mutableListOf<ChannelTopic>()
        for (i in 0 until streamId) {
            resList.add(ChannelTopic((1..300).random(), topics.random()))
        }
        return resList
    }
}
