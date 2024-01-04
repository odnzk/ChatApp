package com.study.channels.data

import com.study.channels.data.mapper.toChannelTopicEntities
import com.study.channels.data.mapper.toChannelTopics
import com.study.channels.domain.model.ChannelTopic
import com.study.channels.data.mapper.mapToIsChannelAlreadyExistBoolean
import com.study.channels.data.mapper.toChannelEntity
import com.study.channels.domain.repository.ChannelRepository
import com.study.channels.data.mapper.toChannelEntityList
import com.study.channels.data.mapper.toChannels
import com.study.channels.data.source.local.LocalChannelDataSource
import com.study.channels.data.source.remote.RemoteChannelDataSource
import com.study.channels.domain.model.Channel
import com.study.channels.domain.model.ChannelAlreadyExistsException
import com.study.channels.domain.model.ChannelDoesNotHaveTopicsException
import com.study.common.ext.runCatchingNonCancellation
import com.study.network.model.ConnectionLostException
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

    override suspend fun addChannel(channel: Channel) {
        if (localDS.getChannelByTitle(channel.title) != null) throw ChannelAlreadyExistsException()
        val entity = channel.toChannelEntity()
        runCatchingNonCancellation {
            remoteDS.addChannel(channel.title)
        }.onSuccess { response ->
            if (response.mapToIsChannelAlreadyExistBoolean()) {
                remoteDS.unsubscribeFromChannel(channel.title)
                throw ChannelAlreadyExistsException()
            } else updateChannels()
        }.onFailure {
            localDS.deleteChannel(entity)
            throw ConnectionLostException()
        }
    }

    private suspend fun updateChannels() {
        val isSubscribed = true
        val remoteChannels = remoteDS.getChannels(isSubscribed).toChannelEntityList(isSubscribed)
        localDS.updateChannels(remoteChannels, isSubscribed)
    }
}
