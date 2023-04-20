package com.study.users.di

import com.study.network.repository.UserDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface UsersDep {
    val searchFlow: Flow<String>
    val dispatcher: CoroutineDispatcher
    val userDataSource: UserDataSource
}
