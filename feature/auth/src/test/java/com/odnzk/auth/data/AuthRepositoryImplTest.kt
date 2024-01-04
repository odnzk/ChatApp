package com.odnzk.auth.data

import com.study.auth.api.Authentificator
import com.study.network.model.response.user.ApiKeyResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

private const val EMAIL = "email@gmail.com"
private const val PASSWORD = "1111"
private const val API_KEY = "dcjwejcbfewucivfdvhieduchew9uifhc"
private const val USER_ID = 999
private const val FULL_NAME = "Name Surname"

@RunWith(JUnit4::class)
internal class AuthRepositoryImplTest {

    private val datasource: RemoteAuthDataSource = mockk()
    private val authentificator: Authentificator = spyk()
    private val authRepositoryImpl = AuthRepositoryImpl(datasource, authentificator)

    @Test
    fun `test login`() {
        val response = ApiKeyResponse(API_KEY, USER_ID)
        coEvery { datasource.login(EMAIL, PASSWORD) } returns response

        runTest {
            authRepositoryImpl.login(EMAIL, PASSWORD)
        }

        coVerify { datasource.login(EMAIL, PASSWORD) }
        coVerify { authentificator.saveUserId(USER_ID) }
        coVerify { authentificator.saveApiKey(API_KEY) }
        coVerify { authentificator.saveEmail(EMAIL) }
    }

    @Test
    fun `test signup`() {
        coEvery { datasource.signup(EMAIL, PASSWORD, FULL_NAME) } returns Unit

        runTest { authRepositoryImpl.signup(EMAIL, PASSWORD, FULL_NAME) }

        coVerify { datasource.signup(EMAIL, PASSWORD, FULL_NAME) }
    }
}