package com.study.network.di

import com.study.network.repository.MessageDataSource
import com.study.network.repository.StreamDataSource
import com.study.network.repository.UserDataSource

interface NetworkImpl {
    val messageDataSource: MessageDataSource
    val streamDataSource: StreamDataSource
    val userDataSource: UserDataSource
}
