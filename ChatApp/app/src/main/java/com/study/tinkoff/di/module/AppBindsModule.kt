package com.study.tinkoff.di.module

import com.study.components.util.AndroidResourcesProvider
import com.study.components.util.ResourcesProvider
import dagger.Binds
import dagger.Module

@Module
interface AppBindsModule {

    @Binds
    fun bindsResourcesProvider(impl: AndroidResourcesProvider): ResourcesProvider
}