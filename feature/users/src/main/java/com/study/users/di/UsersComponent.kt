package com.study.users.di

import com.study.common.di.FeatureScope
import com.study.users.presentation.UsersFragment
import com.study.users.presentation.elm.UsersEffect
import com.study.users.presentation.elm.UsersEvent
import com.study.users.presentation.elm.UsersState
import dagger.Component
import vivid.money.elmslie.android.storeholder.StoreHolder

@[FeatureScope Component(
    dependencies = [UsersDep::class],
    modules = [UsersRepositoryModule::class, UsersModule::class]
)]
internal interface UsersComponent {
    fun inject(fragment: UsersFragment)

    val userStoreHolder: StoreHolder<UsersEvent, UsersEffect, UsersState>

    @Component.Factory
    interface Factory {
        fun create(dependencies: UsersDep): UsersComponent
    }
}

