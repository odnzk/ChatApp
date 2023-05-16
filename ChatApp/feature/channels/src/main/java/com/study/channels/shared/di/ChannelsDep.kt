package com.study.channels.shared.di

import android.content.Context
import com.study.database.dao.ChannelDao
import com.study.database.dao.ChannelTopicDao
import com.study.network.ZulipApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface ChannelsDep {
    val context: Context
    val searchFlow: Flow<String>
    val dispatcher: CoroutineDispatcher
    val zulipApi: ZulipApi
    val channelDao: ChannelDao
    val topicDao: ChannelTopicDao
}
