package com.study.chat.data

import com.study.auth.api.UserAuthRepository

class StubUserAuthRepository : UserAuthRepository {
    override suspend fun getUserId(): Int = TEST_USER_ID

    companion object {
        private const val TEST_USER_ID = 9
    }
}
