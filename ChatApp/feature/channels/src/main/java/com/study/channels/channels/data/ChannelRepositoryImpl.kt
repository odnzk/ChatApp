package com.study.channels.channels.data

import com.study.channels.channels.data.mapper.toChannelTopicEntities
import com.study.channels.channels.data.mapper.toChannelTopics
import com.study.channels.channels.domain.model.ChannelTopic
import com.study.channels.channels.domain.repository.ChannelRepository
import com.study.channels.shared.data.mapper.toChannelEntityList
import com.study.channels.shared.data.mapper.toChannels
import com.study.channels.shared.data.source.LocalChannelDataSource
import com.study.channels.shared.data.source.RemoteChannelDataSource
import com.study.channels.shared.domain.model.Channel
import com.study.channels.shared.domain.model.ChannelDoesNotHaveTopicsException
import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@Reusable
internal class ChannelRepositoryImpl @Inject constructor(
    private val localDS: LocalChannelDataSource,
    private val remoteDS: RemoteChannelDataSource
) : ChannelRepository {

    override fun getChannels(isSubscribed: Boolean, searchQuery: String): Flow<List<Channel>> =
        localDS
            .getChannels(isSubscribed, searchQuery)
            .map { it.toChannels() }
            .distinctUntilChanged()

    override fun getChannelTopics(channelId: Int): Flow<List<ChannelTopic>> =
        localDS.getChannelTopicsWithMessages(channelId).map { it.toChannelTopics() }

    override suspend fun loadChannels(isSubscribed: Boolean) {
        val remoteChannels = remoteDS.getChannels(isSubscribed).toChannelEntityList(isSubscribed)
        localDS.updateChannels(remoteChannels, isSubscribed)
    }

    override suspend fun loadChannelTopics(channelId: Int) {
        val topics = remoteDS.getChannelTopics(channelId).takeIf { it.topics?.isNotEmpty() == true }
            ?.toChannelTopicEntities(channelId) ?: throw ChannelDoesNotHaveTopicsException()
        localDS.updateTopics(topics, channelId)
    }
}
