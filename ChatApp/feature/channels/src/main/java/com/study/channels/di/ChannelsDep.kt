package com.study.channels.di

import android.content.Context
import com.study.database.dataSource.ChannelLocalDataSource
import com.study.network.dataSource.ChannelRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface ChannelsDep {
    val context: Context
    val searchFlow: Flow<String>
    val dispatcher: CoroutineDispatcher
    val streamDataSource: ChannelRemoteDataSource
    val channelLocalDS: ChannelLocalDataSource
}
