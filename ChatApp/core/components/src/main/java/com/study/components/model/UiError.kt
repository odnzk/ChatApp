package com.study.components.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

class UiError(
    val error: Throwable,
    @StringRes val messageRes: Int,
    @StringRes val descriptionRes: Int? = null,
    @DrawableRes val imageRes: Int? = null
)
