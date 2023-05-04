package com.study.network.di

import android.content.Context


interface NetworkDep {
    val context: Context
    val credentials: UserCredentials
    val baseUrl: String
}

class UserCredentials(val username: String, val password: String)
