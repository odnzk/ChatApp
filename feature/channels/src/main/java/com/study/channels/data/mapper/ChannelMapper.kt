package com.study.channels.data.mapper

import com.study.channels.data.source.local.entity.ChannelEntity
import com.study.channels.data.source.local.entity.ChannelUpdateEntity
import com.study.channels.domain.model.Channel
import com.study.network.model.response.stream.AddStreamResponse
import com.study.network.model.response.stream.AllStreamsResponse
import com.study.network.model.response.stream.StreamDto

internal fun List<ChannelEntity>.toChannelUpdateEntity(): List<ChannelUpdateEntity> =
    map { channel ->
        ChannelUpdateEntity(
            id = channel.id,
            title = channel.title
        )
    }

internal fun List<ChannelEntity>.toChannels(): List<Channel> =
    map { Channel(id = it.id, title = it.title, color = it.color) }

internal fun AllStreamsResponse.toChannelEntityList(isSubscribed: Boolean): List<ChannelEntity> =
    streams?.filterNotNull()?.map { it.toChannelEntity(isSubscribed) } ?: emptyList()

private fun StreamDto.toChannelEntity(isSubscribed: Boolean) = ChannelEntity(
    id = streamId,
    title = name,
    isSubscribed = isSubscribed,
    color = if (isSubscribed) requireNotNull(color) else color
)

internal fun AddStreamResponse.mapToIsChannelAlreadyExistBoolean() =
    alreadySubscribed?.isNotEmpty() ?: false

internal fun Channel.toChannelEntity(): ChannelEntity =
    ChannelEntity(id = id, title = title, isSubscribed = true, color = color)