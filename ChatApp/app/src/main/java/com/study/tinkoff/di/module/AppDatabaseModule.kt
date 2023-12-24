package com.study.tinkoff.di.module

import android.content.Context
import com.study.database.dao.ChannelDao
import com.study.database.dao.ChannelTopicDao
import com.study.database.dao.MessageDao
import com.study.database.dao.ReactionDao
import com.study.database.di.DatabaseDep
import com.study.database.di.DatabaseProvider
import com.study.database.di.DatabaseProviderFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppDatabaseModule {

    @Provides
    @Singleton
    fun providesDatabaseDep(context: Context): DatabaseDep = object : DatabaseDep {
        override val context: Context = context
    }

    @Provides
    @Singleton
    fun providesDatabaseImpl(dep: DatabaseDep): DatabaseProvider = DatabaseProviderFactory.create(dep)

    @Provides
    fun providesMessageDao(impl: DatabaseProvider): MessageDao = impl.messageDao

    @Provides
    fun providesReactionDao(impl: DatabaseProvider): ReactionDao = impl.reactionDao

    @Provides
    fun providesChannelDao(impl: DatabaseProvider): ChannelDao = impl.channelDao

    @Provides
    fun providesTopicDao(impl: DatabaseProvider): ChannelTopicDao = impl.topicDao

}
