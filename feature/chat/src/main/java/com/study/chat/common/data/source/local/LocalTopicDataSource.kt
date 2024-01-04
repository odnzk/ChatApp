package com.study.chat.common.data.source.local

import com.study.database.dao.ChannelTopicDao
import com.study.database.model.ChannelTopicEntity
import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@Reusable
internal class LocalTopicDataSource @Inject constructor(
    private val topicDao: ChannelTopicDao
) {

    fun getChannelTopics(channelId: Int): Flow<List<ChannelTopicEntity>> =
        topicDao.getTopicsByChannelId(channelId)

    suspend fun updateTopics(topics: List<ChannelTopicEntity>, channelId: Int) =
        topicDao.updateTopics(topics, channelId)

}
