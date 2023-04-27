package com.study.profile.di

import com.study.network.dataSource.UserDataSource
import kotlinx.coroutines.CoroutineDispatcher

interface ProfileDep {
    val dispatcher: CoroutineDispatcher
    val userDataSource: UserDataSource
}
