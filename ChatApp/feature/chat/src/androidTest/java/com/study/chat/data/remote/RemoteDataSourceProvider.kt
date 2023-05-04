package com.study.chat.data.remote

import android.content.Context
import com.study.chat.di.GeneralDepContainer
import com.study.network.di.NetworkDep
import com.study.network.di.NetworkImplFactory
import com.study.network.di.UserCredentials
import okhttp3.mockwebserver.MockWebServer

class RemoteDataSourceProvider {

    fun createServer() = MockWebServer().apply {
        dispatcher = MockRequestDispatcher()
    }

    fun createNetworkDep(server: MockWebServer) = object : NetworkDep {
        override val context: Context = GeneralDepContainer.applicationContext
        override val credentials: UserCredentials = UserCredentials("", "")
        override val baseUrl: String = server.url("/").toString()
    }

    fun provide(networkDep: NetworkDep) =
        NetworkImplFactory.create(networkDep).messageDataSource
}
