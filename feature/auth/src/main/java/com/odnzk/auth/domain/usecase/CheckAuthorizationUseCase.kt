package com.odnzk.auth.domain.usecase

import com.study.auth.api.Authentificator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class CheckAuthorizationUseCase @Inject constructor(
    private val authentificator: Authentificator,
    private val dispather: CoroutineDispatcher
) {
    suspend operator fun invoke(): Boolean = withContext(dispather) {
        authentificator.isAuthorized()
    }
}