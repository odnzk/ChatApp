package com.study.profile.presentation.util.mapper

import com.study.components.model.UserPresenceStatus
import com.study.profile.domain.model.UserDetailed
import com.study.profile.presentation.util.model.UiUser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private val mapperDefaultDispatcher = Dispatchers.Default

internal suspend fun UserDetailed.toUiUser(
    presence: UserPresenceStatus,
    dispatcher: CoroutineDispatcher = mapperDefaultDispatcher
): UiUser = withContext(dispatcher) {
    UiUser(username = name, avatarUrl = avatarUrl, presence = presence)
}
