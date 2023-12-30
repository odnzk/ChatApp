package com.study.users.di

import com.study.common.di.FeatureScope
import com.study.components.di.SingletoneStoreHolder
import com.study.users.presentation.elm.UsersActor
import com.study.users.presentation.elm.UsersEffect
import com.study.users.presentation.elm.UsersEvent
import com.study.users.presentation.elm.UsersReducer
import com.study.users.presentation.elm.UsersState
import dagger.Module
import dagger.Provides
import vivid.money.elmslie.android.storeholder.StoreHolder
import vivid.money.elmslie.coroutines.ElmStoreCompat

@Module
internal class UsersModule {

    @Provides
    @FeatureScope
    fun providesStoreHolder(
        reducer: UsersReducer,
        actor: UsersActor
    ): StoreHolder<UsersEvent, UsersEffect, UsersState> =
        SingletoneStoreHolder { ElmStoreCompat(UsersState(), reducer, actor) }

}
