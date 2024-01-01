package com.study.auth.impl.di

import com.study.auth.api.UserAuthRepository
import com.study.auth.api.di.AuthProvider
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
internal class AuthModule {

    @Reusable
    @Provides
    fun providesAuthImpl(userAuthRepository: UserAuthRepository): AuthProvider =
        object : AuthProvider {
            override val userAuthRepository: UserAuthRepository = userAuthRepository
        }
}
