package com.study.auth.impl.di

import com.study.auth.api.Authentificator
import com.study.auth.impl.DefaultAuthentificator
import dagger.Binds
import dagger.Module
import dagger.Reusable

@Module
internal interface AuthRepositoryModule {
    @Binds
    @Reusable
    fun bindsUserAuthRepository(impl: DefaultAuthentificator): Authentificator
}
