package com.study.chat.data.remote

import android.content.Context
import com.study.auth.api.Authentificator
import com.study.chat.di.GeneralDepContainer
import com.study.network.di.NetworkDep
import com.study.network.di.NetworkProviderFactory
import io.mockk.mockk
import okhttp3.mockwebserver.MockWebServer

class RemoteDataSourceProvider {

    fun createServer() = MockWebServer().apply {
        dispatcher = MockRequestDispatcher()
    }

    fun createNetworkDep() = object : NetworkDep {
        override val context: Context = GeneralDepContainer.applicationContext
        override val authentificator: Authentificator = mockk()
    }

    fun provide(networkDep: NetworkDep) = NetworkProviderFactory.create(networkDep)
}
