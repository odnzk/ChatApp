package com.study.chat.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.study.channels.data.source.local.ChannelTopicDao
import com.study.chat.common.data.source.local.message.dao.MessageDao
import com.study.chat.common.data.source.local.message.dao.ReactionDao
import com.study.chat.common.data.source.local.message.entity.MessageEntity
import com.study.chat.common.data.source.local.message.entity.ReactionEntity
import com.study.tinkoff.di.CalendarConverter


@Database(
    entities = [
        MessageEntity::class,
        ReactionEntity::class
    ],
    exportSchema = false,
    version = 1
)
@TypeConverters(com.study.tinkoff.di.CalendarConverter::class)
internal abstract class MessageTestDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
    abstract fun reactionDao(): ReactionDao
    abstract fun topicDao(): com.study.channels.data.source.local.ChannelTopicDao
}


