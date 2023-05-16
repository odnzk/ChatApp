package com.study.chat.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.study.chat.shared.data.source.local.LocalMessageDataSource
import com.study.common.extension.fastLazy
import com.study.database.dao.MessageDao
import com.study.database.dao.ReactionDao

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

