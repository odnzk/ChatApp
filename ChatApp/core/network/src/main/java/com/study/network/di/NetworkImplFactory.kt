package com.study.network.di

object NetworkImplFactory {
    fun create(dependencies: NetworkDep): NetworkImpl = DaggerNetworkComponent.factory()
        .create(dependencies)
        .networkImpl
}
