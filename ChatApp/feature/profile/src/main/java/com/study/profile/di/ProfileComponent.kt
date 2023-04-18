package com.study.profile.di

import com.study.common.FeatureScope
import com.study.network.repository.UserDataSource
import com.study.profile.presentation.ProfileFragment
import dagger.Component
import kotlinx.coroutines.CoroutineDispatcher

@[FeatureScope Component(
    dependencies = [ProfileDeps::class],
    modules = [ProfileRepositoryModule::class, ProfileModule::class]
)]
internal interface ProfileComponent {
    fun inject(fragment: ProfileFragment)

    @Component.Factory
    interface Factory {
        fun create(dependencies: ProfileDeps): ProfileComponent
    }
}

interface ProfileDeps {
    val dispatcher: CoroutineDispatcher
    val userDataSource: UserDataSource
}
