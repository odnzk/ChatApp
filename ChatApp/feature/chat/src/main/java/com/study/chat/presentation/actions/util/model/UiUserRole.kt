package com.study.chat.presentation.actions.util.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
internal enum class UiUserRole(val allowedActions: List<UiAction>) : Parcelable {
    USER(listOf(UiAction.COPY, UiAction.ADD_REACTION)),
    ADMIN(listOf(UiAction.COPY, UiAction.ADD_REACTION, UiAction.DELETE)),
    OWNER((listOf(UiAction.COPY, UiAction.ADD_REACTION, UiAction.DELETE, UiAction.EDIT))),
    UNDEFINED(listOf())
}
