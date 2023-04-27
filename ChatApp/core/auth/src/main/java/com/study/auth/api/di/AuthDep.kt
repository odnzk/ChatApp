package com.study.auth.api.di

import android.content.Context
import com.study.network.dataSource.UserDataSource
import kotlinx.coroutines.CoroutineDispatcher

interface AuthDep {
    val dispatcher: CoroutineDispatcher
    val userDataSource: UserDataSource
    val context: Context
}
