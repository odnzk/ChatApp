package com.study.profile.di

import com.study.auth.api.UserAuthRepository
import com.study.network.api.UsersApi
import kotlinx.coroutines.CoroutineDispatcher

interface ProfileDep {
    val userAuthRepository: UserAuthRepository
    val dispatcher: CoroutineDispatcher
    val profileApi: UsersApi
}
