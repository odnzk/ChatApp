package com.study.tinkoff.di.module

import com.odnzk.auth.di.MainFeaturesStarter
import com.study.components.util.AndroidResourcesProvider
import com.study.components.util.ResourcesProvider
import com.study.tinkoff.di.MainFeaturesStarterImpl
import dagger.Binds
import dagger.Module

@Module
interface AppBindsModule {

    @Binds
    fun bindsResourcesProvider(impl: AndroidResourcesProvider): ResourcesProvider

    @Binds
    fun bindsMainFeaturesStarter(impl: MainFeaturesStarterImpl): MainFeaturesStarter
}
