package com.study.components.extensions

import android.content.Context
import com.study.ui.R
import java.util.concurrent.CancellationException

fun Throwable.handle(context: Context): String {
    val resId = when (this) {
        else -> R.string.error_unknown
    }
    return context.getString(resId)
}

inline fun <R> runCatchingNonCancellation(block: () -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }
}



