package com.study.channels.data.source.local

import com.study.channels.data.source.local.dao.ChannelDao
import com.study.channels.data.source.local.dao.ChannelTopicDao
import com.study.channels.data.source.local.entity.ChannelEntity
import com.study.channels.data.source.local.entity.ChannelTopicEntity
import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@Reusable
internal class LocalChannelDataSource @Inject constructor(
    private val channelDao: ChannelDao,
    private val topicDao: ChannelTopicDao
) {

    fun getChannels(isSubscribed: Boolean, query: String): Flow<List<ChannelEntity>> =
        channelDao.getChannels(isSubscribed, query)

    fun getChannelTopicsWithMessages(channelId: Int): Flow<List<ChannelTopicEntity>> =
        topicDao.getTopicsByChannelId(channelId)

    suspend fun getChannelByTitle(title: String): ChannelEntity? =
        channelDao.getChannelByTitle(title)

    suspend fun updateChannels(channels: List<ChannelEntity>, isSubscribed: Boolean) =
        channelDao.updateChannels(channels, isSubscribed)

    suspend fun updateTopics(topics: List<ChannelTopicEntity>, channelId: Int) =
        topicDao.updateTopics(topics, channelId)

    suspend fun addChannel(channel: ChannelEntity) = channelDao.insertChannel(channel)

    suspend fun deleteChannel(channel: ChannelEntity) = channelDao.delete(channel)

}
