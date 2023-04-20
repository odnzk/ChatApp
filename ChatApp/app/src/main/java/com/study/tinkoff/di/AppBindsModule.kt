package com.study.tinkoff.di

import dagger.Binds
import dagger.Module
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

@Module
interface AppBindsModule {
    @Binds
    fun bindsSearchFlow(@MutableSearchFlow impl: MutableSharedFlow<String>): Flow<String>
}
