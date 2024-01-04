package com.study.components.model

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

class UiError(
    val error: Throwable,
    @StringRes val messageRes: Int,
    @StringRes val descriptionRes: Int? = null,
    @DrawableRes val imageRes: Int? = null,
    val descriptionArgs: Any? = null
) {
    fun getMessage(context: Context): String = context.getString(messageRes)

    fun getDescription(context: Context): String? =
        descriptionRes?.let { context.getString(descriptionRes, descriptionArgs) }

}
