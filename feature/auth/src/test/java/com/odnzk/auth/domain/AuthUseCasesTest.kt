package com.odnzk.auth.domain

import com.odnzk.auth.domain.repository.AuthRepository
import com.odnzk.auth.domain.usecase.CheckAuthorizationUseCase
import com.odnzk.auth.domain.usecase.LoginUseCase
import com.odnzk.auth.domain.usecase.SignupUseCase
import com.study.auth.api.Authentificator
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class AuthUseCasesTest {

    private val authentificator: Authentificator = mockk()
    private val dispatcher = UnconfinedTestDispatcher()
    private val checkAuthorizationUseCase = CheckAuthorizationUseCase(authentificator, dispatcher)

    private val authRepository: AuthRepository = spyk()
    private val loginUseCase = LoginUseCase(authRepository, dispatcher)
    private val signupUseCase: SignupUseCase = SignupUseCase(authRepository, dispatcher)

    private companion object {
        const val EMAIL = "email"
        const val PASSWORD = "1111"
        const val FULL_NAME = "user"
    }

    @Test
    fun `test checking authorization when user is not authorized`() {
        val expected = false
        coEvery { authentificator.isAuthorized() } returns expected

        runTest {
            val actual = checkAuthorizationUseCase()

            coVerify { authentificator.isAuthorized() }
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `test checking authorization when user is authorized`() {
        val expected = true
        coEvery { authentificator.isAuthorized() } returns expected

        runTest {
            val actual = checkAuthorizationUseCase()

            coVerify { authentificator.isAuthorized() }
            assertEquals(expected, actual)
        }
    }


    @Test
    fun `test login use case`() {
        runTest {
            loginUseCase(email = EMAIL, password = PASSWORD)
        }

        coVerify { authRepository.login(email = EMAIL, password = PASSWORD) }
    }

    @Test
    fun `test signup use case`() {
        runTest {
            signupUseCase(email = EMAIL, password = PASSWORD, fullName = FULL_NAME)
        }

        coVerify { authRepository.signup(email = EMAIL, password = PASSWORD, fullName = FULL_NAME) }
    }

}