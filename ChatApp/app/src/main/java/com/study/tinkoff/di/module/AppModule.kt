package com.study.tinkoff.di.module

import com.study.tinkoff.di.SearchFlow
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
import vivid.money.elmslie.core.store.Store
import vivid.money.elmslie.coroutines.ElmStoreCompat
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    @Singleton
    fun providesStore(
        reducer: MainReducer,
        actor: MainActor
    ): Store<MainEvent, MainEffect, MainState> = ElmStoreCompat(MainState(), reducer, actor)

    @Provides
    @Singleton
    @SearchFlow
    fun providesSearchFlow(): MutableSharedFlow<String> = MutableSharedFlow()

    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.Default
}
