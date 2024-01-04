package com.study.chat.common.di

import com.study.chat.common.data.source.local.message.dao.MessageDao
import com.study.chat.common.data.source.local.message.dao.ReactionDao

interface ChatDatabase {

    fun messagesDao(): MessageDao

    fun reactionsDao(): ReactionDao
}