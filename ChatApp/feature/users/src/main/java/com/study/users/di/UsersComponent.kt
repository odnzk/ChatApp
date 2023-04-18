package com.study.users.di

import com.study.common.FeatureScope
import com.study.users.presentation.UsersFragment
import dagger.Component

@[FeatureScope Component(
    dependencies = [UsersDep::class],
    modules = [UsersRepositoryModule::class, UsersModule::class]
)]
internal interface UsersComponent {
    fun inject(fragment: UsersFragment)

    @Component.Factory
    interface Factory {
        fun create(dependencies: UsersDep): UsersComponent
    }
}

