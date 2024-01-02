package com.odnzk.auth.di

import androidx.annotation.RestrictTo
import androidx.lifecycle.ViewModel
import kotlin.properties.Delegates.notNull

internal interface FeatureAuthDepsProvider {

    @get:RestrictTo(RestrictTo.Scope.LIBRARY)
    val dep: FeatureAuthDep

    companion object : FeatureAuthDepsProvider by FeatureFeatureAuthDepStore
}

object FeatureFeatureAuthDepStore : FeatureAuthDepsProvider {
    override var dep: FeatureAuthDep by notNull()
}

internal class FeatureAuthComponentViewModel : ViewModel() {
    val component = DaggerFeatureAuthComponent.factory().create(FeatureAuthDepsProvider.dep)
}
