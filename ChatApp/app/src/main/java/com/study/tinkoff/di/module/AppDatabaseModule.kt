package com.study.tinkoff.di.module

import android.content.Context
import com.study.database.dataSource.ChannelLocalDataSource
import com.study.database.dataSource.MessageLocalDataSource
import com.study.database.di.DatabaseDep
import com.study.database.di.DatabaseImpl
import com.study.database.di.DatabaseImplFactory
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
    fun providesDatabaseImpl(dep: DatabaseDep): DatabaseImpl =
        DatabaseImplFactory.create(dep)

    @Provides
    fun providesMessageLocalDS(impl: DatabaseImpl): MessageLocalDataSource = impl.messageDataSource

    @Provides
    fun providesChannelLocalDS(impl: DatabaseImpl): ChannelLocalDataSource = impl.channelDataSource

}
