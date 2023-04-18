package com.study.chat.di

import com.study.auth.api.UserAuthRepository
import com.study.network.repository.MessageDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface ChatDep {
    val dispatcher: CoroutineDispatcher
    val searchFlow: Flow<String>
    val messageDataSource: MessageDataSource
    val userAuthRepository: UserAuthRepository
}
