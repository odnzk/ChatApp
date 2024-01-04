package com.study.auth.api.di

import android.content.Context
import kotlinx.coroutines.CoroutineDispatcher

interface AuthDep {
    val dispatcher: CoroutineDispatcher
    val context: Context
}
