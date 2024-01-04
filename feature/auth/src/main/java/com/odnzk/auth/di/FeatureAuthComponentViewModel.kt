package com.odnzk.auth.di

import androidx.annotation.RestrictTo
import androidx.lifecycle.ViewModel
import kotlin.properties.Delegates.notNull

internal interface FeatureAuthDepsProvider {

    @get:RestrictTo(RestrictTo.Scope.LIBRARY)
    val dep: FeatureAuthDep

    companion object : FeatureAuthDepsProvider by FeatureAuthDepStore
}

object FeatureAuthDepStore : FeatureAuthDepsProvider {
    override var dep: FeatureAuthDep by notNull()
}

internal class FeatureAuthComponentViewModel : ViewModel() {
    val component by lazy {
        DaggerFeatureAuthComponent.factory().create(FeatureAuthDepsProvider.dep)
    }

    override fun onCleared() {
        super.onCleared()
        component.authStoreHolder.store.stop()
        component.loginStoreHolder.store.stop()
        component.signupStoreHolder.store.stop()
    }
}
