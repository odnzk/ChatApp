package com.study.auth.impl.di

import com.study.auth.api.Authentificator
import com.study.auth.api.di.AuthProvider
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
internal class AuthModule {

    @Reusable
    @Provides
    fun providesAuthImpl(authentificator: Authentificator): AuthProvider =
        object : AuthProvider {
            override val authentificator: Authentificator = authentificator
        }
}
