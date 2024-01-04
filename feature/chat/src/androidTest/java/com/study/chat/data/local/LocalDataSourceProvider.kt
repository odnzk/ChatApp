package com.study.chat.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.study.chat.common.data.source.local.message.LocalMessageDataSource
import com.study.common.ext.fastLazy
import com.study.chat.common.data.source.local.message.dao.MessageDao
import com.study.chat.common.data.source.local.message.dao.ReactionDao

internal class LocalDataSourceProvider {
    private val db by fastLazy {
        Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(), MessageTestDatabase::class.java
        ).build()
    }
    private val defaultMessageDao = db.messageDao()
    private val defaultReactionDao = db.reactionDao()

    fun provide(
        messageDao: MessageDao = defaultMessageDao, reactionDao: ReactionDao = defaultReactionDao
    ) = LocalMessageDataSource(messageDao, reactionDao)
}

