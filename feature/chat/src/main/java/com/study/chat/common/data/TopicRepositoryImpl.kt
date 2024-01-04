package com.study.chat.common.data

import com.study.chat.common.data.mapper.toChannelTopics
import com.study.chat.common.data.source.remote.RemoteTopicDataSource
import com.study.chat.common.domain.repository.TopicRepository
import dagger.Reusable
import javax.inject.Inject

@Reusable
internal class TopicRepositoryImpl @Inject constructor(
    private val remoteDS: RemoteTopicDataSource
) : TopicRepository {

    override suspend fun getChannelTopicsTitles(channelId: Int): List<String> =
        remoteDS.getChannelTopics(channelId).toChannelTopics()
}
