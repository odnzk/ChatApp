package com.odnzk.auth.domain.usecase

import com.study.auth.api.Authentificator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CheckAuthorizationUseCase @Inject constructor(private val authentificator: Authentificator) {
    suspend operator fun invoke(): Boolean = withContext(Dispatchers.IO) {
        authentificator.isAuthorized()
    }
}