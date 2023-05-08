package com.study.chat.domain.repository

import kotlinx.coroutines.flow.Flow

interface ChannelRepository {
    fun getChannelTopicsTitles(channelId: Int): Flow<List<String>>
    suspend fun loadChannelTopics(channelId: Int)
}
