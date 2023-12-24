package com.study.channels.common.di

import android.content.Context
import com.study.database.dao.ChannelDao
import com.study.database.dao.ChannelTopicDao
import com.study.network.api.ChannelsApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface ChannelsDep {
    val context: Context
    val searchFlow: Flow<String>
    val dispatcher: CoroutineDispatcher
    val channelsApi: ChannelsApi
    val channelDao: ChannelDao
    val topicDao: ChannelTopicDao
}
