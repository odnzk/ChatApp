package com.study.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.study.database.dao.ChannelDao
import com.study.database.dao.ChannelTopicDao
import com.study.database.dao.MessageDao
import com.study.database.dao.ReactionDao
import com.study.database.model.ChannelEntity
import com.study.database.model.ChannelTopicEntity
import com.study.database.model.MessageEntity
import com.study.database.model.ReactionEntity
import com.study.database.util.CalendarConverter

@Database(
    entities = [
        ChannelEntity::class,
        ChannelTopicEntity::class,
        MessageEntity::class,
        ReactionEntity::class
    ],
    exportSchema = false,
    version = 1
)
@TypeConverters(CalendarConverter::class)
internal abstract class AppDatabase : RoomDatabase() {

    abstract fun channelDao(): ChannelDao
    abstract fun topicDao(): ChannelTopicDao
    abstract fun messageDao(): MessageDao
    abstract fun reactionDao(): ReactionDao
}
