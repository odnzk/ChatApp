package com.odnzk.auth.di

import com.odnzk.auth.data.AuthRepositoryImpl
import com.odnzk.auth.domain.repository.AuthRepository
import com.study.components.util.AndroidResourcesProvider
import com.study.components.util.ResourcesProvider
import dagger.Binds
import dagger.Module

@Module
internal interface BindsModule {
    @Binds
    fun bindsResourcesProvider(impl: AndroidResourcesProvider): ResourcesProvider

    @Binds
    fun bindsAUthRepository(impl: AuthRepositoryImpl): AuthRepository
}