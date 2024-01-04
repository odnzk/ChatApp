package com.study.chat.common.domain.repository

internal interface TopicRepository {
    suspend fun getChannelTopicsTitles(channelId: Int): List<String>
}
