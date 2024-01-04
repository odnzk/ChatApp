package com.study.tinkoff.di.module

import com.odnzk.auth.di.MainFeaturesStarter
import com.study.channels.di.ChannelsDatabase
import com.study.chat.common.di.ChatDatabase
import com.study.components.util.AndroidResourcesProvider
import com.study.components.util.ResourcesProvider
import com.study.tinkoff.di.AppDatabase
import com.study.tinkoff.util.MainFeaturesStarterImpl
import dagger.Binds
import dagger.Module

@Module
interface AppBindsModule {

    @Binds
    fun bindsResourcesProvider(impl: AndroidResourcesProvider): ResourcesProvider

    @Binds
    fun bindsMainFeaturesStarter(impl: MainFeaturesStarterImpl): MainFeaturesStarter

    @Binds
    fun bindsChannelsDatabase(impl: AppDatabase): ChannelsDatabase

    @Binds
    fun bindsChatDatabase(impl: AppDatabase): ChatDatabase
}
