package com.study.database.di

import android.content.Context
import androidx.room.Room
import com.study.database.AppDatabase
import com.study.database.dao.ChannelDao
import com.study.database.dao.ChannelTopicDao
import com.study.database.dao.MessageDao
import com.study.database.dao.ReactionDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal class DatabaseModule {

    @Provides
    @Singleton
    fun providesDatabaseImpl(
        messageDao: MessageDao,
        reactionDao: ReactionDao,
        channelDao: ChannelDao,
        topicDao: ChannelTopicDao
    ): DatabaseImpl = object : DatabaseImpl {
        override val messageDao: MessageDao = messageDao
        override val reactionDao: ReactionDao = reactionDao
        override val channelDao: ChannelDao = channelDao
        override val topicDao: ChannelTopicDao = topicDao
    }

    @Provides
    @Singleton
    fun providesDatabase(context: Context) =
        Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun providesMessageDao(database: AppDatabase): MessageDao = database.messageDao()

    @Provides
    @Singleton
    fun providesChannelDao(database: AppDatabase): ChannelDao = database.channelDao()

    @Provides
    @Singleton
    fun providesChannelTopicDao(database: AppDatabase): ChannelTopicDao = database.topicDao()

    @Provides
    @Singleton
    fun providesReactionDao(database: AppDatabase): ReactionDao = database.reactionDao()

    companion object {
        private const val DATABASE_NAME = "zulip_database"
    }
}
