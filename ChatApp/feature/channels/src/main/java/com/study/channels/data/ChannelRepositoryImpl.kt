package com.study.channels.data

import com.study.channels.data.mapper.mapToIsChannelAlreadyExistBoolean
import com.study.channels.data.mapper.toChannelEntity
import com.study.channels.data.mapper.toChannelEntityList
import com.study.channels.data.mapper.toChannelTopicEntities
import com.study.channels.data.mapper.toChannelTopics
import com.study.channels.data.mapper.toChannels
import com.study.channels.domain.exceptions.ChannelAlreadyExistsException
import com.study.channels.domain.model.Channel
import com.study.channels.domain.model.ChannelTopic
import com.study.channels.domain.repository.ChannelRepository
import com.study.common.extension.runCatchingNonCancellation
import com.study.database.dataSource.ChannelLocalDataSource
import com.study.network.dataSource.ChannelRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class ChannelRepositoryImpl @Inject constructor(
    private val localDS: ChannelLocalDataSource,
    private val remoteDS: ChannelRemoteDataSource
) : ChannelRepository {

    override fun getChannels(isSubscribed: Boolean): Flow<List<Channel>> =
        localDS
            .getChannels(isSubscribed)
            .map { it.toChannels() }
            .distinctUntilChanged()

    override fun getChannelTopics(channelId: Int): Flow<List<ChannelTopic>> =
        localDS.getChannelTopics(channelId).map { it.toChannelTopics() }

    override suspend fun loadChannels(isSubscribed: Boolean) {
        localDS.updateChannels(
            remoteDS.getChannels(isSubscribed).toChannelEntityList(isSubscribed),
            isSubscribed
        )
    }

    override suspend fun loadChannelTopics(channelId: Int) {
        val topics = remoteDS.getChannelTopics(channelId).toChannelTopicEntities(channelId)
        localDS.updateTopics(topics, channelId)
    }

    override suspend fun addChannel(channel: Channel) {
        if (localDS.getChannelByTitle(channel.title) != null) throw ChannelAlreadyExistsException()
        val entity = channel.toChannelEntity()
        localDS.addChannel(entity)
        runCatchingNonCancellation {
            remoteDS.addChannel(channel.title)
        }.onSuccess { response ->
            if (response.mapToIsChannelAlreadyExistBoolean()) {
                remoteDS.unsubscribeFromChannel(channel.title)
                throw ChannelAlreadyExistsException()
            }
        }.onFailure {
            localDS.deleteChannel(entity)
        }
    }
}
