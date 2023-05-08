package com.study.chat.data

import com.study.chat.data.mapper.toChannelTopicEntities
import com.study.chat.data.mapper.toTopicTitles
import com.study.chat.domain.repository.ChannelRepository
import com.study.database.dataSource.ChannelLocalDataSource
import com.study.network.dataSource.ChannelRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class ChannelRepositoryImpl @Inject constructor(
    private val remoteDS: ChannelRemoteDataSource,
    private val localDS: ChannelLocalDataSource
) : ChannelRepository {

    override fun getChannelTopicsTitles(channelId: Int): Flow<List<String>> =
        localDS.getChannelTopics(channelId).map { it.toTopicTitles() }

    override suspend fun loadChannelTopics(channelId: Int) {
        val topics = remoteDS.getChannelTopics(channelId).toChannelTopicEntities(channelId)
        localDS.updateTopics(topics, channelId)
    }
}
