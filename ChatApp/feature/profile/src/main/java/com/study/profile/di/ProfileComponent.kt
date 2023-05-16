package com.study.profile.di

import com.study.common.di.FeatureScope
import com.study.profile.presentation.ProfileFragment
import com.study.profile.presentation.elm.ProfileEffect
import com.study.profile.presentation.elm.ProfileEvent
import com.study.profile.presentation.elm.ProfileState
import dagger.Component
import vivid.money.elmslie.android.storeholder.StoreHolder

@[FeatureScope Component(
    dependencies = [ProfileDep::class],
    modules = [ProfileRepositoryModule::class, ProfileModule::class]
)]
internal interface ProfileComponent {
    fun inject(fragment: ProfileFragment)

    val profileStoreHolder: StoreHolder<ProfileEvent, ProfileEffect, ProfileState>

    @Component.Factory
    interface Factory {
        fun create(dependencies: ProfileDep): ProfileComponent
    }
}
