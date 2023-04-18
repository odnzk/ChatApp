package com.study.channels.data

import com.study.channels.data.mapper.toChannelList
import com.study.channels.data.mapper.toChannelTopicList
import com.study.channels.domain.model.Channel
import com.study.channels.domain.model.ChannelTopic
import com.study.channels.domain.repository.ChannelRepository
import com.study.network.repository.StreamDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class RemoteChannelRepository @Inject constructor(
    private val dataSource: StreamDataSource,
    private val dispatcher: CoroutineDispatcher
) : ChannelRepository {
    override suspend fun getAllChannels(): List<Channel> = withContext(dispatcher) {
        dataSource.getAllStreams().toChannelList()
    }

    override suspend fun getSubscribedChannels(): List<Channel> = withContext(dispatcher) {
        dataSource.getSubscribedStreams().toChannelList()
    }

    override suspend fun getChannelTopics(streamId: Int): List<ChannelTopic> =
        withContext(dispatcher)
        { dataSource.getStreamTopics(streamId).toChannelTopicList() }
}
