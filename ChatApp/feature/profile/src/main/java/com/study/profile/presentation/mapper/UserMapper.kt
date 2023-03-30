package com.study.profile.presentation.mapper

import com.study.profile.domain.model.UserDetailed
import com.study.profile.presentation.model.UiUser
import com.study.ui.R
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private val mapperDefaultDispatcher = Dispatchers.Default

internal suspend fun UserDetailed.toUiUser(
    dispatcher: CoroutineDispatcher = mapperDefaultDispatcher
): UiUser = withContext(dispatcher) {
    val activeStatusId =
        if (isActive) R.string.user_is_active_status_online else R.string.user_is_active_status_offline
    val isOnMeetingStatus =
        if (isActive) R.string.user_is_on_meeting_status_true else R.string.user_is_on_meeting_status_false
    UiUser(
        username = name,
        avatarUrl = avatarUrl,
        isOnMeetingStatus = "on meeting",
        isActiveStatus = "online"
    )
}
