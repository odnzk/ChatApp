package com.study.auth.impl.di

import com.study.auth.api.di.AuthDep
import com.study.auth.api.di.AuthImpl
import dagger.Component
import javax.inject.Singleton

@Component(
    dependencies = [AuthDep::class],
    modules = [AuthModule::class, AuthRepositoryModule::class]
)
@Singleton
internal interface AuthComponent {

    val authImpl: AuthImpl

    @Component.Factory
    interface Factory {
        fun create(dependencies: AuthDep): AuthComponent
    }
}
