package com.odnzk.auth.di

import com.odnzk.auth.presentation.AuthActivity
import com.study.common.di.FeatureScope
import dagger.Component

@[FeatureScope Component(
    dependencies = [FeatureAuthDep::class],
    modules = [BindsModule::class]
)]
internal interface FeatureAuthComponent {
    fun inject(activity: AuthActivity)

    @Component.Factory
    interface Factory {
        fun create(dependencies: FeatureAuthDep): FeatureAuthComponent
    }
}
