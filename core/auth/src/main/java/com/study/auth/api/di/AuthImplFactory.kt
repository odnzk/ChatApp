package com.study.auth.api.di

import com.study.auth.impl.di.DaggerAuthComponent

object AuthImplFactory {
    fun create(dependencies: AuthDep): AuthProvider =
        DaggerAuthComponent.factory().create(dependencies).authProvider
}
