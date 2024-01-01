package com.study.profile.di

import androidx.annotation.RestrictTo
import androidx.lifecycle.ViewModel
import kotlin.properties.Delegates.notNull

internal interface ProfileDepsProvider {

    @get:RestrictTo(RestrictTo.Scope.LIBRARY)
    val dep: ProfileDep

    companion object : ProfileDepsProvider by ProfileDepStore
}

object ProfileDepStore : ProfileDepsProvider {
    override var dep: ProfileDep by notNull()
}

internal class ProfileComponentViewModel : ViewModel() {
    val profileComponent = DaggerProfileComponent.factory().create(ProfileDepsProvider.dep)
}
