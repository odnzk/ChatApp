package com.odnzk.auth.di

import android.content.Context
import com.study.auth.api.Authentificator
import com.study.network.api.AuthApi
import kotlinx.coroutines.CoroutineDispatcher

interface FeatureAuthDep {
    val authentificator: Authentificator
    val authApi: AuthApi
    val context: Context
    val mainFeaturesStarter: MainFeaturesStarter
    val dispatcher: CoroutineDispatcher
}