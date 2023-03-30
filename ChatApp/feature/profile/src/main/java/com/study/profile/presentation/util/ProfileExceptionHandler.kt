package com.study.profile.presentation.util

import android.content.Context
import com.study.profile.R
import com.study.profile.domain.util.ProfileException
import com.study.profile.domain.util.UserNotAuthorizedException
import com.study.profile.domain.util.UserNotFoundException

internal fun ProfileException.handle(context: Context): String {
    val resId = when (this) {
        is UserNotAuthorizedException -> R.string.error_user_not_authorized
        is UserNotFoundException -> R.string.error_user_not_found
    }
    return context.getString(resId)
}
