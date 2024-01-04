package com.study.tinkoff.di

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.study.channels.data.source.local.dao.ChannelDao
import com.study.channels.data.source.local.dao.ChannelTopicDao
import com.study.channels.data.source.local.entity.ChannelEntity
import com.study.channels.data.source.local.entity.ChannelTopicEntity
import com.study.channels.di.ChannelsDatabase
import com.study.chat.common.data.source.local.message.dao.MessageDao
import com.study.chat.common.data.source.local.message.dao.ReactionDao
import com.study.chat.common.data.source.local.message.entity.MessageEntity
import com.study.chat.common.data.source.local.message.entity.ReactionEntity
import com.study.chat.common.di.ChatDatabase
import com.study.tinkoff.util.CalendarConverter

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
abstract class AppDatabase : RoomDatabase(), ChannelsDatabase, ChatDatabase {
    abstract override fun channelsDao(): ChannelDao
    abstract override fun topicsDao(): ChannelTopicDao
    abstract override fun messagesDao(): MessageDao
    abstract override fun reactionsDao(): ReactionDao

    companion object{
        const val DATABASE_NAME = "zulip_database"
    }
}
