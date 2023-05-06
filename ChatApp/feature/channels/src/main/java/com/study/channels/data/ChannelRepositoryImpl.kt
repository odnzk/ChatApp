package com.study.channels.data

import com.study.channels.data.mapper.mapToIsChannelAlreadyExistBoolean
import com.study.channels.data.mapper.toChannelEntityList
import com.study.channels.data.mapper.toChannelTopicEntities
import com.study.channels.data.mapper.toChannelTopics
import com.study.channels.data.mapper.toChannels
import com.study.channels.domain.exceptions.ChannelAlredyExistsException
import com.study.channels.domain.model.Channel
import com.study.channels.domain.model.ChannelFilter
import com.study.channels.domain.model.ChannelTopic
import com.study.channels.domain.repository.ChannelRepository
import com.study.database.dataSource.ChannelLocalDataSource
import com.study.database.entity.ChannelEntity
import com.study.network.dataSource.ChannelRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.random.Random

internal class ChannelRepositoryImpl @Inject constructor(
    private val localDS: ChannelLocalDataSource,
    private val remoteDS: ChannelRemoteDataSource
) : ChannelRepository {

    override fun getChannels(channelFilter: ChannelFilter): Flow<List<Channel>> =
        localDS
            .getChannels(channelFilter.isSubscribedOnly())
            .map { it.toChannels() }
            .distinctUntilChanged()

    override fun getChannelTopics(channelId: Int): Flow<List<ChannelTopic>> =
        localDS.getChannelTopics(channelId).map { it.toChannelTopics() }

    override suspend fun loadChannels(channelFilter: ChannelFilter) {
        val isSubscribed = channelFilter.isSubscribedOnly()
        localDS.updateChannels(
            remoteDS.getChannels(isSubscribed).toChannelEntityList(isSubscribed),
            isSubscribed
        )
    }

    override suspend fun loadChannelTopics(channelId: Int) {
        val topics = remoteDS.getChannelTopics(channelId).toChannelTopicEntities(channelId)
        localDS.updateTopics(topics, channelId)
    }

    override suspend fun addChannel(title: String, isHistoryPublic: Boolean) {
        if (remoteDS.addChannel(title, isHistoryPublic).mapToIsChannelAlreadyExistBoolean()) {
            remoteDS.unsubscribeFromChannel(title)
            throw ChannelAlredyExistsException()
        } else {
            localDS.insertChannel(ChannelEntity(id = Random.nextInt(), title, true))
        }
    }

    private fun ChannelFilter.isSubscribedOnly(): Boolean = when (this) {
        ChannelFilter.ALL -> false
        ChannelFilter.SUBSCRIBED_ONLY -> true
    }
}
