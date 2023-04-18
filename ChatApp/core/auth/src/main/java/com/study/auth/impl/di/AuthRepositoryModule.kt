package com.study.auth.impl.di

import com.study.auth.api.UserAuthRepository
import com.study.auth.impl.DefaultUserAuthRepository
import dagger.Binds
import dagger.Module
import dagger.Reusable

@Module
internal interface AuthRepositoryModule {

    @Binds
    @Reusable
    fun bindsUserAuthRepository(impl: DefaultUserAuthRepository): UserAuthRepository
}
