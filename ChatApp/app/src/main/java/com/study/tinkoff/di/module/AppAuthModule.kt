package com.study.tinkoff.di.module

import android.content.Context
import com.study.auth.api.UserAuthRepository
import com.study.auth.api.di.AuthDep
import com.study.auth.api.di.AuthImplFactory
import com.study.auth.api.di.AuthProvider
import com.study.network.api.AuthApi
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
class AppAuthModule {

    @Provides
    @Singleton
    fun providesAuthDependencies(
        dispatcher: CoroutineDispatcher,
        api: AuthApi,
        context: Context
    ): AuthDep = object : AuthDep {
        override val dispatcher: CoroutineDispatcher = dispatcher
        override val authApi: AuthApi = api
        override val context: Context = context
    }

    @Provides
    @Singleton
    fun providesAuthImpl(authDep: AuthDep): AuthProvider = AuthImplFactory.create(authDep)

    @Provides
    fun providesAuthRepository(impl: AuthProvider): UserAuthRepository = impl.userAuthRepository

}
