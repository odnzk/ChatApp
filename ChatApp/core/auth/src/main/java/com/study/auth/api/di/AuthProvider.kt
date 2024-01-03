package com.study.auth.api.di

import com.study.auth.api.Authentificator

interface AuthProvider {
    val authentificator: Authentificator
}
