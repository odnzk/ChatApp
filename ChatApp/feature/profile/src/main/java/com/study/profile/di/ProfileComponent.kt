package com.study.profile.di

import com.study.common.di.FeatureScope
import com.study.profile.presentation.ProfileFragment
import dagger.Component

@[FeatureScope Component(
    dependencies = [ProfileDep::class],
    modules = [ProfileRepositoryModule::class, ProfileModule::class]
)]
internal interface ProfileComponent {
    fun inject(fragment: ProfileFragment)

    @Component.Factory
    interface Factory {
        fun create(dependencies: ProfileDep): ProfileComponent
    }
}
