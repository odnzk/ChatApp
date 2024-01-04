package com.odnzk.auth.data

import com.study.network.api.AuthApi
import com.study.network.model.request.user.CreateUserRequest
import com.study.network.model.response.user.ApiKeyResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
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
class RemoteAuthDataSourceTest {

    private val authApi: AuthApi = mockk()
    private val datasource: RemoteAuthDataSource = RemoteAuthDataSource(authApi)

    @Test
    fun `test login`() {
        val expected = ApiKeyResponse(API_KEY, USER_ID)
        coEvery { authApi.fetchApiKey(username = EMAIL, password = PASSWORD) } returns expected

        val actual = runBlocking {
            datasource.login(EMAIL, PASSWORD)
        }

        coVerify { authApi.fetchApiKey(EMAIL, PASSWORD) }
        assertEquals("api keys do not match", expected, actual)
        assertEquals("user id do not match", expected, actual)
    }

    @Test
    fun `test signup`() {
        val data = CreateUserRequest(email = EMAIL, password = PASSWORD, fullName = FULL_NAME)
        coEvery { authApi.createUser(data) } returns Unit

        runTest {
            datasource.signup(email = EMAIL, password = PASSWORD, fullName = FULL_NAME)
        }

        coVerify { authApi.createUser(data) }
    }


}