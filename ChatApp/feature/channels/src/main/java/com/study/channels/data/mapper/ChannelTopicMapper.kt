package com.study.channels.data.mapper

import com.study.channels.domain.model.ChannelTopic
import com.study.network.impl.model.response.stream.StreamTopicsResponse
import com.study.network.impl.model.response.stream.TopicDto

private fun TopicDto.toChannelTopic(): ChannelTopic = ChannelTopic(
    lastMessageId = requireNotNull(maxId),
    title = requireNotNull(name)
)

internal fun StreamTopicsResponse.toChannelTopicList(): List<ChannelTopic> =
    topics?.filterNotNull()?.map { it.toChannelTopic() } ?: emptyList()

