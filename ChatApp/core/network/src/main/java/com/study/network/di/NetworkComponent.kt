package com.study.network.di

import dagger.Component
import javax.inject.Singleton


@Component(
    dependencies = [NetworkDep::class],
    modules = [NetworkModule::class]
)
@Singleton
internal interface NetworkComponent {
    @Component.Factory
    interface Factory {
        fun create(networkDep: NetworkDep): NetworkComponent
    }

    val networkImpl: NetworkImpl
}
