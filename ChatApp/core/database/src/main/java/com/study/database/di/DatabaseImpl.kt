package com.study.database.di

import com.study.database.dataSource.ChannelLocalDataSource
import com.study.database.dataSource.MessageLocalDataSource

interface DatabaseImpl {
    val messageDataSource: MessageLocalDataSource
    val channelDataSource: ChannelLocalDataSource
}
