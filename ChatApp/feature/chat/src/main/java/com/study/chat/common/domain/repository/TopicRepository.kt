package com.study.chat.common.domain.repository

import kotlinx.coroutines.flow.Flow

internal interface TopicRepository {
    fun getChannelTopicsTitles(channelId: Int): Flow<List<String>>
    suspend fun loadChannelTopics(channelId: Int)
}
