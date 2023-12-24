package com.study.channels.common.data.mapper

import com.study.channels.common.domain.model.Channel
import com.study.database.model.ChannelEntity
import com.study.network.model.response.stream.AddStreamResponse
import com.study.network.model.response.stream.AllStreamsResponse
import com.study.network.model.response.stream.StreamDto

internal fun List<ChannelEntity>.toChannels(): List<Channel> =
    map { Channel(id = it.id, title = it.title, color = it.color) }

internal fun AllStreamsResponse.toChannelEntityList(isSubscribed: Boolean): List<ChannelEntity> =
    streams?.filterNotNull()?.map { it.toChannelEntity(isSubscribed) } ?: emptyList()

private fun StreamDto.toChannelEntity(isSubscribed: Boolean) = ChannelEntity(
    id = requireNotNull(streamId),
    title = requireNotNull(name),
    isSubscribed = isSubscribed,
    color = if (isSubscribed) requireNotNull(color) else color
)

internal fun AddStreamResponse.mapToIsChannelAlreadyExistBoolean() =
    alreadySubscribed?.isNotEmpty() ?: false

internal fun Channel.toChannelEntity(): ChannelEntity =
    ChannelEntity(id = id, title = title, isSubscribed = true, color = color)
