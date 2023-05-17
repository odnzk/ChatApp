package com.study.channels.addChannel.data

import com.study.channels.addChannel.domain.repository.AddChannelRepository
import com.study.channels.shared.data.mapper.mapToIsChannelAlreadyExistBoolean
import com.study.channels.shared.data.mapper.toChannelEntity
import com.study.channels.shared.data.source.LocalChannelDataSource
import com.study.channels.shared.data.source.RemoteChannelDataSource
import com.study.channels.shared.domain.model.Channel
import com.study.channels.shared.domain.model.ChannelAlreadyExistsException
import com.study.common.extension.runCatchingNonCancellation
import com.study.network.model.ConnectionLostException
import dagger.Reusable
import javax.inject.Inject

@Reusable
internal class AddChannelRepositoryImpl @Inject constructor(
    private val localDS: LocalChannelDataSource,
    private val remoteDS: RemoteChannelDataSource
) : AddChannelRepository {

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
            throw ConnectionLostException()
        }
    }
}
