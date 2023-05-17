package com.study.network.di

import dagger.Component
import javax.inject.Singleton


@Component(
    dependencies = [NetworkDep::class],
    modules = [NetworkModule::class]
)
@Singleton
internal interface NetworkComponent {
    val networkImpl: NetworkImpl

    @Component.Factory
    interface Factory {
        fun create(networkDep: NetworkDep): NetworkComponent
    }
}
