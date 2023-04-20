package com.study.auth.api.di

import com.study.auth.api.UserAuthRepository

interface AuthImpl {
    val userAuthRepository: UserAuthRepository
}
