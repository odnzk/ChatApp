package com.study.components.model

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.study.components.R
import com.study.ui.R as CoreR

// todo remove from :components
enum class UiUserPresenceStatus(@StringRes val titleResId: Int, @ColorRes val colorResId: Int) {
    ACTIVE(R.string.user_status_active, CoreR.color.apple_green),
    IDLE(R.string.user_status_idle, CoreR.color.romegranate),
    OFFLINE(R.string.user_status_offline, CoreR.color.dark_red),
    BOT(R.string.user_status_bot, CoreR.color.purple_light)
}
