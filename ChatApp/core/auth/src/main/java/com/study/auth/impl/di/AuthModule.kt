package com.study.auth.impl.di

import com.study.auth.api.UserAuthRepository
import com.study.auth.api.di.AuthImpl
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
internal class AuthModule {

    @Reusable
    @Provides
    fun providesAuthImpl(userAuthRepository: UserAuthRepository): AuthImpl = object : AuthImpl {
        override val userAuthRepository: UserAuthRepository = userAuthRepository
    }
}
