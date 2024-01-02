package com.odnzk.auth.di

import dagger.Binds
import dagger.Module

@Module
internal interface BindsModule {
    @Binds
    fun bindsResourcesProvider(impl: com.study.components.util.AndroidResourcesProvider): com.study.components.util.ResourcesProvider
}