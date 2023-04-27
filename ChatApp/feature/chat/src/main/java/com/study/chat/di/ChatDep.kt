package com.study.chat.di

import com.study.auth.api.UserAuthRepository
import com.study.database.dataSource.MessageLocalDataSource
import com.study.network.dataSource.MessageRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface ChatDep {
    val dispatcher: CoroutineDispatcher
    val searchFlow: Flow<String>
    val messageRemoteDataSource: MessageRemoteDataSource
    val messageLocalDataSource: MessageLocalDataSource
    val userAuthRepository: UserAuthRepository
}
