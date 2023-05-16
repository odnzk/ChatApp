package com.study.channels.channels.data.mapper

import com.study.channels.channels.domain.model.ChannelTopic
import com.study.database.model.ChannelTopicEntity
import com.study.network.model.response.stream.StreamTopicsResponse
import com.study.network.model.response.stream.TopicDto

internal fun List<ChannelTopicEntity>.toChannelTopics() = map { ChannelTopic(title = it.title) }

private fun TopicDto.toChannelTopicEntity(channelId: Int): ChannelTopicEntity = ChannelTopicEntity(
    title = requireNotNull(name), channelId = channelId, id = requireNotNull(maxId)
)

internal fun StreamTopicsResponse.toChannelTopicEntities(channelId: Int): List<ChannelTopicEntity> =
    topics?.filterNotNull()?.map { it.toChannelTopicEntity(channelId) } ?: emptyList()


