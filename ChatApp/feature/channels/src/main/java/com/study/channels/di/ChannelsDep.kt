package com.study.channels.di

import android.content.Context
import com.study.network.repository.StreamDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface ChannelsDep {
    val context: Context
    val searchFlow: Flow<String>
    val dispatcher: CoroutineDispatcher
    val streamDataSource: StreamDataSource
}
