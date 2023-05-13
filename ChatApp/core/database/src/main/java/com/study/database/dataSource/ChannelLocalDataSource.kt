package com.study.database.dataSource

import com.study.database.dao.ChannelDao
import com.study.database.dao.ChannelTopicDao
import com.study.database.entity.ChannelEntity
import com.study.database.entity.ChannelTopicEntity
import com.study.database.entity.tuple.TopicWithMessagesTuple
import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@Reusable
class ChannelLocalDataSource @Inject constructor(
    private val channelDao: ChannelDao,
    private val topicDao: ChannelTopicDao
) {

    fun getChannels(isSubscribed: Boolean): Flow<List<ChannelEntity>> =
        channelDao.getChannels(isSubscribed)

    fun getChannelTopics(channelId: Int): Flow<List<ChannelTopicEntity>> =
        topicDao.getTopicsByChannelTitle(channelId)

    fun getChannelTopicsWithMessages(channelId: Int): Flow<List<TopicWithMessagesTuple>> =
        topicDao.getTopicsWithMessagesCount(channelId)

    suspend fun getChannelByTitle(title: String): ChannelEntity? =
        channelDao.getChannelByTitle(title)

    suspend fun updateChannels(channels: List<ChannelEntity>, isSubscribed: Boolean) =
        channelDao.updateChannels(channels, isSubscribed)

    suspend fun updateTopics(topics: List<ChannelTopicEntity>, channelId: Int) =
        topicDao.updateTopics(topics, channelId)

    suspend fun addChannel(channel: ChannelEntity) = channelDao.insertChannel(channel)

    suspend fun deleteChannel(channel: ChannelEntity) = channelDao.delete(channel)

}
