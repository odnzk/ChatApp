package com.study.database.dataSource

import com.study.database.dao.ChannelDao
import com.study.database.dao.ChannelTopicDao
import com.study.database.entity.ChannelEntity
import com.study.database.entity.ChannelTopicEntity
import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@Reusable
class ChannelLocalDataSource @Inject constructor(
    private val channelDao: ChannelDao,
    private val topicDao: ChannelTopicDao
) {

    suspend fun upsertChannels(channels: List<ChannelEntity>) = channelDao.upsertChannels(channels)

    fun getChannels(isSubscribed: Boolean): Flow<List<ChannelEntity>> =
        channelDao.getChannels(isSubscribed)

    suspend fun getChannelTopics(channelId: Int) = topicDao.getTopicsByChannelTitle(channelId)

    suspend fun upsertTopics(topics: List<ChannelTopicEntity>) = topicDao.upsertTopics(topics)

}
