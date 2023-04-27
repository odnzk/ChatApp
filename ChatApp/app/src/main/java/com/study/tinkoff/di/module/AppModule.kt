package com.study.tinkoff.di.module

import com.study.tinkoff.di.MutableSearchFlow
import com.study.tinkoff.elm.MainActor
import com.study.tinkoff.elm.MainEffect
import com.study.tinkoff.elm.MainEvent
import com.study.tinkoff.elm.MainReducer
import com.study.tinkoff.elm.MainState
import dagger.Module
import dagger.Provides
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
    ): Store<MainEvent, MainEffect, MainState> {
        return ElmStoreCompat(MainState(), reducer, actor)
    }

    @Provides
    @Singleton
    @MutableSearchFlow
    fun providesSearchFlow(): MutableSharedFlow<String> = MutableSharedFlow()

    @Provides
    fun provideCoroutineScope() = Dispatchers.Default

}
