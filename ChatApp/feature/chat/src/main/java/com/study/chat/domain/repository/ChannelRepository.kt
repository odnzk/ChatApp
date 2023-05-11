package com.study.chat.domain.repository

import kotlinx.coroutines.flow.Flow

internal interface ChannelRepository {
    fun getChannelTopicsTitles(channelId: Int): Flow<List<String>>
    suspend fun loadChannelTopics(channelId: Int)
}
