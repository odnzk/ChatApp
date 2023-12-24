package com.study.auth.api.di

import android.content.Context
import com.study.network.api.AuthApi
import kotlinx.coroutines.CoroutineDispatcher

interface AuthDep {
    val dispatcher: CoroutineDispatcher
    val authApi: AuthApi
    val context: Context
}
