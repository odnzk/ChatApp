package com.study.chat.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.study.database.dao.MessageDao
import com.study.database.dao.ReactionDao
import com.study.database.model.MessageEntity
import com.study.database.model.ReactionEntity
import com.study.database.util.CalendarConverter


@Database(
    entities = [
        MessageEntity::class,
        ReactionEntity::class
    ],
    exportSchema = false,
    version = 1
)
@TypeConverters(CalendarConverter::class)
abstract class MessageTestDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
    abstract fun reactionDao(): ReactionDao
}


