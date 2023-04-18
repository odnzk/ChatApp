package com.study.auth.api.di

import com.study.auth.impl.di.DaggerAuthComponent

object AuthImplFactory {
    fun create(dependencies: AuthDep): AuthImpl =
        DaggerAuthComponent.factory().create(dependencies).authImpl
}
