package com.study.channels.data.mapper

import com.study.channels.domain.model.Channel
import com.study.network.model.response.stream.AllStreamsResponse
import com.study.network.model.response.stream.StreamDto
import com.study.network.model.response.stream.SubscribedStreamsResponse
import com.study.network.model.response.stream.SubscriptionDto

internal fun AllStreamsResponse.toChannelList(): List<Channel> =
    streams?.filterNotNull()?.map { it.toChannel() } ?: emptyList()

private fun StreamDto.toChannel() = Channel(
    id = requireNotNull(streamId),
    title = requireNotNull(name),
    description = description.orEmpty(),
    isPrivate = requireNotNull(inviteOnly)
)

internal fun SubscribedStreamsResponse.toChannelList(): List<Channel> =
    subscriptions?.filterNotNull()?.map { it.toChannel() } ?: emptyList()

private fun SubscriptionDto.toChannel(): Channel = Channel(
    id = requireNotNull(streamId),
    title = requireNotNull(name),
    description = description.orEmpty(),
    isPrivate = requireNotNull(inviteOnly)
)
