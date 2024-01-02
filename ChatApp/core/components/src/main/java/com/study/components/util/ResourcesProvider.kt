package com.study.components.util

import androidx.annotation.StringRes

interface ResourcesProvider {
    fun getString(@StringRes stringResId: Int): String

    fun getString(@StringRes stringResId: Int, vararg args: Any): String
}