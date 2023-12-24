package com.study.profile.di

import com.study.network.api.UsersApi
import kotlinx.coroutines.CoroutineDispatcher

interface ProfileDep {
    val dispatcher: CoroutineDispatcher
    val profileApi: UsersApi
}
