package com.study.users.di

import com.study.common.di.FeatureScope
import com.study.common.search.Searcher
import com.study.users.domain.model.User
import com.study.users.presentation.elm.UsersActor
import com.study.users.presentation.elm.UsersEffect
import com.study.users.presentation.elm.UsersEvent
import com.study.users.presentation.elm.UsersReducer
import com.study.users.presentation.elm.UsersState
import dagger.Module
import dagger.Provides
import dagger.Reusable
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

    @Provides
    @Reusable
    fun providesUserSearcher(): Searcher<User> = object : Searcher<User> {
        override val searchPredicate: (User, String) -> Boolean = { user, query ->
            user.name.contains(query, ignoreCase = true)
        }
    }
}
