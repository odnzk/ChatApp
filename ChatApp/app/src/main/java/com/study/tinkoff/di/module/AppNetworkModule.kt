package com.study.tinkoff.di.module

import android.content.Context
import coil.ImageLoader
import com.study.network.ZulipApi
import com.study.network.di.NetworkDep
import com.study.network.di.NetworkImpl
import com.study.network.di.NetworkImplFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppNetworkModule {
    @Provides
    @Singleton
    fun providesNetworkDep(context: Context): NetworkDep = object : NetworkDep {
        override val context: Context = context
    }

    @Provides
    @Singleton
    fun providesNetworkImpl(networkDep: NetworkDep): NetworkImpl =
        NetworkImplFactory.create(networkDep)

    @Provides
    fun provideApi(impl: NetworkImpl): ZulipApi = impl.zulipApi

    @Provides
    fun provideCoilLoader(impl: NetworkImpl): ImageLoader = impl.imageLoader
}
