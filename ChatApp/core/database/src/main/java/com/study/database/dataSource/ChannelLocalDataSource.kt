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

    suspend fun updateChannels(channels: List<ChannelEntity>, isSubscribed: Boolean) =
        channelDao.updateChannels(channels, isSubscribed)

    fun getChannels(isSubscribed: Boolean): Flow<List<ChannelEntity>> =
        channelDao.getChannels(isSubscribed)

    fun getChannelTopics(channelId: Int) = topicDao.getTopicsByChannelTitle(channelId)

    suspend fun updateTopics(topics: List<ChannelTopicEntity>, channelId: Int) =
        topicDao.updateTopics(topics, channelId)

    suspend fun insertChannel(channel: ChannelEntity) = channelDao.insertChannel(channel)

}
