package com.study.profile.di

import androidx.annotation.RestrictTo
import androidx.lifecycle.ViewModel
import kotlin.properties.Delegates.notNull

internal interface ProfileDepsProvider {

    @get:RestrictTo(RestrictTo.Scope.LIBRARY)
    val deps: ProfileDeps

    companion object : ProfileDepsProvider by ProfileDepsStore
}

object ProfileDepsStore : ProfileDepsProvider {
    override var deps: ProfileDeps by notNull()
}

internal class ProfileComponentViewModel : ViewModel() {
    val profileComponent = DaggerProfileComponent.factory().create(ProfileDepsProvider.deps)
}
