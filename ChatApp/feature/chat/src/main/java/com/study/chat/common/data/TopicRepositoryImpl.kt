package com.study.chat.common.data

import com.study.chat.common.data.mapper.toChannelTopicEntities
import com.study.chat.common.data.mapper.toTopicTitles
import com.study.chat.common.data.source.local.LocalTopicDataSource
import com.study.chat.common.data.source.remote.RemoteTopicDataSource
import com.study.chat.common.domain.repository.TopicRepository
import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@Reusable
internal class TopicRepositoryImpl @Inject constructor(
    private val remoteDS: RemoteTopicDataSource,
    private val localDS: LocalTopicDataSource
) : TopicRepository {

    override fun getChannelTopicsTitles(channelId: Int): Flow<List<String>> =
        localDS.getChannelTopics(channelId).map { it.toTopicTitles() }

    override suspend fun loadChannelTopics(channelId: Int) {
        val topics = remoteDS.getChannelTopics(channelId).toChannelTopicEntities(channelId)
        localDS.updateTopics(topics, channelId)
    }
}
