package com.study.channels.data.mapper

import com.study.channels.domain.model.ChannelTopic
import com.study.database.entity.ChannelTopicEntity
import com.study.network.model.response.stream.StreamTopicsResponse
import com.study.network.model.response.stream.TopicDto

internal fun List<ChannelTopicEntity>.toChannelTopics() = map { ChannelTopic(title = it.title) }
private fun TopicDto.toChannelTopicEntity(channelId: Int): ChannelTopicEntity = ChannelTopicEntity(
    title = requireNotNull(name), channelId = channelId, id = requireNotNull(maxId)
)

internal fun StreamTopicsResponse.toChannelTopicEntities(channelId: Int): List<ChannelTopicEntity> =
    topics?.filterNotNull()?.map { it.toChannelTopicEntity(channelId) } ?: emptyList()

private fun TopicDto.toChannelTopic(): ChannelTopic = ChannelTopic(title = requireNotNull(name))
internal fun StreamTopicsResponse.toChannelTopics(): List<ChannelTopic> =
    topics?.filterNotNull()?.map { it.toChannelTopic() } ?: emptyList()


