package com.study.network.di

import android.content.Context
import com.study.auth.api.Authentificator

interface NetworkDep {
    val context: Context
    val authentificator: Authentificator
}
