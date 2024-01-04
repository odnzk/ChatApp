package com.study.network.di

object NetworkProviderFactory {
    fun create(dependencies: NetworkDep): NetworkProvider = DaggerNetworkComponent.factory()
        .create(dependencies)
        .networkProvider
}
