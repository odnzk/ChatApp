package com.study.components.model

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.study.components.R
import com.study.ui.R as CoreR

enum class UserPresenceStatus(@StringRes val titleResId: Int, @ColorRes val colorResId: Int) {
    ACTIVE(R.string.user_status_active, CoreR.color.green_light),
    IDLE(R.string.user_status_idle, CoreR.color._test_orange),
    OFFLINE(R.string.user_status_offline, CoreR.color._test_red)
}
