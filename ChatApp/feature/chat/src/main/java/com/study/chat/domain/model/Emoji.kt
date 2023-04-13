package com.study.chat.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Emoji(
    val name: String,
    val code: String
) : Parcelable
