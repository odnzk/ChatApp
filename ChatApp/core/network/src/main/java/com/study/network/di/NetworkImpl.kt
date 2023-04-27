package com.study.network.di

import com.study.network.dataSource.ChannelRemoteDataSource
import com.study.network.dataSource.MessageRemoteDataSource
import com.study.network.dataSource.UserDataSource

interface NetworkImpl {
    val messageDataSource: MessageRemoteDataSource
    val streamDataSource: ChannelRemoteDataSource
    val userDataSource: UserDataSource
}
