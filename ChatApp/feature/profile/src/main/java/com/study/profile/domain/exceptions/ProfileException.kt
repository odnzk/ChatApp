package com.study.profile.domain.exceptions

import com.study.components.extensions.UserFriendlyError
import com.study.components.extensions.toBaseErrorMessage
import com.study.profile.R

internal sealed class ProfileException : RuntimeException()
internal class UserNotFoundException : ProfileException()

private fun ProfileException.toErrorMessage(): UserFriendlyError {
    val (messageRes, descriptionRes, imageRes) = when (this) {
        is UserNotFoundException -> Triple(
            R.string.error_user_not_found,
            R.string.error_description_user_not_found,
            com.study.components.R.drawable.ic_not_found
        )
    }
    return UserFriendlyError(this, messageRes, descriptionRes, imageRes)
}

internal fun Throwable.toErrorMessage(): UserFriendlyError {
    return if (this is ProfileException) toErrorMessage() else toBaseErrorMessage()
}
