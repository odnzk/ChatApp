package com.study.channels.data.mapper

import com.study.channels.domain.model.Channel
import com.study.database.entity.ChannelEntity
import com.study.network.model.response.stream.AddStreamResponse
import com.study.network.model.response.stream.AllStreamsResponse
import com.study.network.model.response.stream.StreamDto

internal fun List<ChannelEntity>.toChannels(): List<Channel> = map { Channel(it.id, it.title) }

internal fun AllStreamsResponse.toChannelEntityList(isSubscribed: Boolean): List<ChannelEntity> =
    streams?.filterNotNull()?.map { it.toChannelEntity(isSubscribed) } ?: emptyList()

private fun StreamDto.toChannelEntity(isSubscribed: Boolean) = ChannelEntity(
    id = requireNotNull(streamId),
    title = requireNotNull(name),
    isSubscribed = isSubscribed
)

internal fun AddStreamResponse.mapToIsChannelAlreadyExistBoolean() =
    alreadySubscribed?.isNotEmpty() ?: false
