package com.study.network.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConnectionManager @Inject constructor(private val context: Context) {
    private var isNetworkAvailable: Boolean = false

    init {
        registerNetworkCallback()
    }

    fun isConnected(): Boolean = isNetworkAvailable

    @SuppressLint("MissingPermission")
    private fun registerNetworkCallback() {
        try {
            val connectivityManager = context.getSystemService(ConnectivityManager::class.java)
            connectivityManager?.registerDefaultNetworkCallback(object : NetworkCallback() {
                override fun onLost(network: Network) {
                    isNetworkAvailable = false
                }

                override fun onAvailable(network: Network) {
                    isNetworkAvailable = true
                }
            })
        } catch (ex: Exception) {
            isNetworkAvailable = false
        }
    }

}
