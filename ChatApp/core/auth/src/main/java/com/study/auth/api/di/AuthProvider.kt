package com.study.auth.api.di

import com.study.auth.api.UserAuthRepository

interface AuthProvider {
    val userAuthRepository: UserAuthRepository
}
