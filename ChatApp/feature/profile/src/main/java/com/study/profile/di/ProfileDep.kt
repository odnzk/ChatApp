package com.study.profile.di

import com.study.network.ZulipApi
import kotlinx.coroutines.CoroutineDispatcher

interface ProfileDep {
    val dispatcher: CoroutineDispatcher
    val api: ZulipApi
}
