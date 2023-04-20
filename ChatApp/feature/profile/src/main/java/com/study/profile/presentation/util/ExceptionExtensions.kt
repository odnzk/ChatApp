package com.study.profile.presentation.util

import com.study.components.extensions.UiError
import com.study.components.extensions.toBaseErrorMessage
import com.study.profile.R
import com.study.profile.domain.exceptions.UserNotFoundException
import com.study.components.R as ComponentsR

internal fun Throwable.toErrorMessage(): UiError = when (this) {
    is UserNotFoundException -> UiError(
        this,
        R.string.error_user_not_found,
        R.string.error_description_user_not_found,
        ComponentsR.drawable.ic_not_found
    )
    else -> toBaseErrorMessage()
}
