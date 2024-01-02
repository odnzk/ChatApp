package com.study.chat.data

import com.study.auth.api.Authentificator

class StubAuthentificator : Authentificator {
    override suspend fun getUserId(): Int = TEST_USER_ID
    override suspend fun isAdmin(): Boolean = true

    companion object {
        private const val TEST_USER_ID = 9
    }
}
