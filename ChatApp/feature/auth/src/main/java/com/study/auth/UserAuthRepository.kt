package com.study.auth

import android.content.Context
import com.study.network.impl.ZulipApi

interface UserAuthRepository {
    suspend fun getCurrentUserId(): Int

    companion object {
        operator fun invoke(api: ZulipApi, context: Context): UserAuthRepository =
            DefaultUserAuthRepository(api, context)
    }
}
