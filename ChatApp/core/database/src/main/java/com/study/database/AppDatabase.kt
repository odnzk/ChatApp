package com.study.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.study.database.converter.CalendarConverter
import com.study.database.dao.ChannelDao
import com.study.database.dao.ChannelTopicDao
import com.study.database.dao.MessageDao
import com.study.database.dao.ReactionDao
import com.study.database.entity.ChannelEntity
import com.study.database.entity.ChannelTopicEntity
import com.study.database.entity.MessageEntity
import com.study.database.entity.ReactionEntity

@Database(
    entities = [
        ChannelEntity::class,
        ChannelTopicEntity::class,
        MessageEntity::class,
        ReactionEntity::class
    ],
    version = 1
)
@TypeConverters(CalendarConverter::class)
internal abstract class AppDatabase : RoomDatabase() {

    abstract fun channelDao(): ChannelDao
    abstract fun topicDao(): ChannelTopicDao
    abstract fun messageDao(): MessageDao
    abstract fun reactionDao(): ReactionDao
}
