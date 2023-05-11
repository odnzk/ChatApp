package com.study.chat.presentation.util.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UiEmoji(
    val name: String,
    val code: String
) : Parcelable
