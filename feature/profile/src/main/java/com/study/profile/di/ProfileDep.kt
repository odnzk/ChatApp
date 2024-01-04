package com.study.profile.di

import com.study.auth.api.Authentificator
import com.study.network.api.UsersApi
import kotlinx.coroutines.CoroutineDispatcher

interface ProfileDep {
    val authentificator: Authentificator
    val dispatcher: CoroutineDispatcher
    val profileApi: UsersApi
}
