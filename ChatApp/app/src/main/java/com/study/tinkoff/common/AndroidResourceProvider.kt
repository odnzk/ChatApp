package com.study.tinkoff.common

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.StringRes

class AndroidResourceProvider(private val context: Context) : ResourceProvider {
    override fun getColor(@ColorRes colorId: Int): Int {
        return context.getColor(colorId)
    }

    override fun getString(@StringRes stringId: Int): String {
        return context.getString(stringId)
    }
}
