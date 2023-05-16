package com.study.chat.shared.di

import android.content.Context
import com.study.auth.api.UserAuthRepository
import com.study.database.dao.ChannelTopicDao
import com.study.database.dao.MessageDao
import com.study.database.dao.ReactionDao
import com.study.network.ZulipApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface ChatDep {
    val dispatcher: CoroutineDispatcher
    val searchFlow: Flow<String>
    val zulipApi: ZulipApi
    val messageDao: MessageDao
    val reactionDao: ReactionDao
    val topicDao: ChannelTopicDao
    val userAuthRepository: UserAuthRepository
    val applicationContext: Context
}
