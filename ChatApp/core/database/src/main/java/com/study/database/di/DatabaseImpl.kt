package com.study.database.di

import com.study.database.dao.ChannelDao
import com.study.database.dao.ChannelTopicDao
import com.study.database.dao.MessageDao
import com.study.database.dao.ReactionDao

interface DatabaseImpl {
    val messageDao: MessageDao
    val reactionDao: ReactionDao
    val channelDao: ChannelDao
    val topicDao: ChannelTopicDao
}
