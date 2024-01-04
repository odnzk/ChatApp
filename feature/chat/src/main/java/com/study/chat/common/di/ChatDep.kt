package com.study.chat.common.di

import android.content.Context
import com.study.auth.api.Authentificator
import com.study.network.api.ChannelsApi
import com.study.network.api.MessagesApi
import com.study.network.api.UsersApi
import kotlinx.coroutines.CoroutineDispatcher

interface ChatDep {
    val dispatcher: CoroutineDispatcher
    val messagesApi: MessagesApi
    val channelsApi: ChannelsApi
    val usersApi: UsersApi
    val authentificator: Authentificator
    val applicationContext: Context
    val chatDatabase: ChatDatabase
}
