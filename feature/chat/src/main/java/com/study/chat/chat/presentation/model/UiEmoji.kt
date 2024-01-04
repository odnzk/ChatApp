package com.study.chat.chat.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UiEmoji(
    val name: String,
    val code: String
) : Parcelable
