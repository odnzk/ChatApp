package com.study.channels.di

import android.content.Context
import com.study.channels.data.source.local.dao.ChannelDao
import com.study.channels.data.source.local.dao.ChannelTopicDao
import com.study.network.api.ChannelsApi
import kotlinx.coroutines.CoroutineDispatcher

interface ChannelsDep {
    val context: Context
    val dispatcher: CoroutineDispatcher
    val channelsApi: ChannelsApi
    val channelsDatabase: ChannelsDatabase
}
