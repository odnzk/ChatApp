package com.study.common.extensions

import android.os.Build
import android.os.Bundle
import android.widget.ImageView

fun ImageView.loadUrl(url: String) = Unit

inline fun <reified T> Bundle.safeGetParcelable(key: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelable(key, T::class.java)
    } else {
        getParcelable(key)
    }
}

