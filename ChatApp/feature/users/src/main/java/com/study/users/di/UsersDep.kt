package com.study.users.di

import com.study.network.api.UsersApi
import kotlinx.coroutines.CoroutineDispatcher

interface UsersDep {
    val dispatcher: CoroutineDispatcher
    val api: UsersApi
}
