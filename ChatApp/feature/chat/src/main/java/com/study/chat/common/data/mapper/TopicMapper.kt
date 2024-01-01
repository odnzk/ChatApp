package com.study.chat.common.data.mapper

import com.study.database.model.ChannelTopicEntity
import com.study.network.model.response.stream.StreamTopicsResponse
import com.study.network.model.response.stream.TopicDto

internal fun List<ChannelTopicEntity>.toTopicTitles() = map { it.title }

private fun TopicDto.toChannelTopicEntity(channelId: Int): ChannelTopicEntity = ChannelTopicEntity(
    title = name, channelId = channelId, id = maxId
)

internal fun StreamTopicsResponse.toChannelTopicEntities(channelId: Int): List<ChannelTopicEntity> =
    topics?.filterNotNull()?.map { it.toChannelTopicEntity(channelId) } ?: emptyList()
