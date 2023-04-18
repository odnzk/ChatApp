package com.study.users.di

import com.study.common.FeatureScope
import com.study.users.presentation.elm.UsersActor
import com.study.users.presentation.elm.UsersEffect
import com.study.users.presentation.elm.UsersEvent
import com.study.users.presentation.elm.UsersReducer
import com.study.users.presentation.elm.UsersState
import dagger.Module
import dagger.Provides
import vivid.money.elmslie.core.store.Store
import vivid.money.elmslie.coroutines.ElmStoreCompat

@Module
internal class UsersModule {
    @Provides
    @FeatureScope
    fun providesStore(
        reducer: UsersReducer,
        actor: UsersActor
    ): Store<UsersEvent, UsersEffect, UsersState> {
        return ElmStoreCompat(UsersState(), reducer, actor)
    }
}
