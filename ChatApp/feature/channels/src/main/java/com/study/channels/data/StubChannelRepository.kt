package com.study.channels.data

import com.study.channels.domain.model.Channel
import com.study.channels.domain.model.ChannelTopic
import com.study.channels.domain.repository.ChannelRepository
import kotlin.random.Random

class StubChannelRepository : ChannelRepository {
    private val channels = RandomChannelGenerator().generate(CHANNELS_COUNT)
    private val topics = listOf("Testing", "Android", "Backend", "Frontend", "Mobile", "UI", "UX")
    override suspend fun getAll(): List<Channel> {
        return if (Random.nextBoolean()) {
            throw RuntimeException()
        } else channels
    }

    override suspend fun getSubscribedStreams(): List<Channel> {
        return if (Random.nextBoolean()) {
            throw RuntimeException()
        } else channels.take(SUBSCRIBED_CHANNELS_COUNT)
    }

    override suspend fun getStreamById(id: Int): Channel? {
        return if (Random.nextBoolean()) {
            throw RuntimeException()
        } else channels.first { it.id == id }
    }

    override suspend fun getStreamByTitle(title: String): List<Channel> {
        return if (Random.nextBoolean()) {
            throw RuntimeException()
        } else channels.filter { it.title == title }
    }

    override suspend fun getStreamTopics(streamId: Int): List<ChannelTopic> {
        val resList = mutableListOf<ChannelTopic>()
        repeat((0 until streamId).count()) {
            resList.add(ChannelTopic(CHANNELS_TOPIC_ID_RANGE.random(), topics.random()))
        }
        return resList
    }

    companion object {
        private const val CHANNELS_COUNT = 5
        private const val SUBSCRIBED_CHANNELS_COUNT = 3
        private val CHANNELS_TOPIC_ID_RANGE = 1..300
    }
}
