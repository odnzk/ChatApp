package com.study.tinkoff.di.module

import com.study.tinkoff.di.SearchFlow
import dagger.Binds
import dagger.Module
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

@Module
interface AppBindsModule {
    @Binds
    fun bindsSearchFlow(@SearchFlow impl: MutableSharedFlow<String>): Flow<String>
}
