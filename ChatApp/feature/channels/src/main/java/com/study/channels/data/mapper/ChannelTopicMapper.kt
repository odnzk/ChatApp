package com.study.channels.data.mapper

import com.study.channels.domain.model.ChannelTopic
import com.study.database.entity.ChannelTopicEntity
import com.study.database.entity.tuple.TopicWithMessagesTuple
import com.study.network.model.response.stream.StreamTopicsResponse
import com.study.network.model.response.stream.TopicDto

internal fun List<TopicWithMessagesTuple>.toChannelTopics() =
    map { ChannelTopic(title = it.topic.title, messagesCount = it.messageCount) }

private fun TopicDto.toChannelTopicEntity(channelId: Int): ChannelTopicEntity = ChannelTopicEntity(
    title = requireNotNull(name), channelId = channelId, id = requireNotNull(maxId)
)

internal fun StreamTopicsResponse.toChannelTopicEntities(channelId: Int): List<ChannelTopicEntity> =
    topics?.filterNotNull()?.map { it.toChannelTopicEntity(channelId) } ?: emptyList()


