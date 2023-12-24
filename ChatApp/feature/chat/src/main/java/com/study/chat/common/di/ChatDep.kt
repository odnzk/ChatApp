package com.study.chat.common.di

import android.content.Context
import com.study.auth.api.UserAuthRepository
import com.study.database.dao.ChannelTopicDao
import com.study.database.dao.MessageDao
import com.study.database.dao.ReactionDao
import com.study.network.api.ChannelsApi
import com.study.network.api.MessagesApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface ChatDep {
    val dispatcher: CoroutineDispatcher
    val searchFlow: Flow<String>
    val messagesApi: MessagesApi
    val channelsApi: ChannelsApi
    val messageDao: MessageDao
    val reactionDao: ReactionDao
    val topicDao: ChannelTopicDao
    val userAuthRepository: UserAuthRepository
    val applicationContext: Context
}
