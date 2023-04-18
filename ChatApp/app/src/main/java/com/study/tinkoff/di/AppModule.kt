package com.study.tinkoff.di

import android.content.Context
import com.study.auth.api.UserAuthRepository
import com.study.auth.api.di.AuthDep
import com.study.auth.api.di.AuthImplFactory
import com.study.network.di.NetworkDep
import com.study.network.di.NetworkImplFactory
import com.study.network.repository.MessageDataSource
import com.study.network.repository.StreamDataSource
import com.study.network.repository.UserDataSource
import com.study.tinkoff.BuildConfig
import com.study.tinkoff.elm.MainActor
import com.study.tinkoff.elm.MainEffect
import com.study.tinkoff.elm.MainEvent
import com.study.tinkoff.elm.MainReducer
import com.study.tinkoff.elm.MainState
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import okhttp3.Credentials
import vivid.money.elmslie.core.store.Store
import vivid.money.elmslie.coroutines.ElmStoreCompat

@Module
class AppModule {
    @Provides
    fun providesNetworkCredentials(): String =
        Credentials.basic(BuildConfig.USERNAME, BuildConfig.PASSWORD)

    @Provides
    @AppScope
    fun providesStore(
        reducer: MainReducer,
        actor: MainActor
    ): Store<MainEvent, MainEffect, MainState> {
        return ElmStoreCompat(MainState(), reducer, actor)
    }

    @Provides
    @AppScope
    @MutableSearchFlow
    fun providesSearchFlow(): MutableSharedFlow<String> = MutableSharedFlow()


    @Provides
    fun providesNetworkDep(credentials: String): NetworkDep = object : NetworkDep {
        override val credentials = credentials
    }

    @Provides
    fun provideCoroutineScope() = Dispatchers.Default

    @Provides
    @AppScope
    fun providesAuthDependencies(
        dispatcher: CoroutineDispatcher,
        userDataSource: UserDataSource,
        context: Context
    ): AuthDep = object : AuthDep {
        override val dispatcher: CoroutineDispatcher = dispatcher
        override val userDataSource: UserDataSource = userDataSource
        override val context: Context = context
    }

    @Provides
    fun provideMessageDataSource(networkDep: NetworkDep): MessageDataSource =
        NetworkImplFactory.create(networkDep).messageDataSource

    @Provides
    fun userAuthRepository(authDep: AuthDep): UserAuthRepository =
        AuthImplFactory.create(authDep).userAuthRepository

    @Provides
    fun provideStreamDataSource(networkDep: NetworkDep): StreamDataSource =
        NetworkImplFactory.create(networkDep).streamDataSource

    @Provides
    fun provideUserDataSource(networkDep: NetworkDep): UserDataSource =
        NetworkImplFactory.create(networkDep).userDataSource
}
