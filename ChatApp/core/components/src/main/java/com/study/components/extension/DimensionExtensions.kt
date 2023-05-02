package com.study.components.extension

import android.content.Context
import android.util.TypedValue
import kotlin.math.roundToInt

fun Float.sp(context: Context) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_SP,
    this,
    context.resources.displayMetrics
)

fun Float.dp(context: Context): Int = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this,
    context.resources.displayMetrics
).roundToInt()

fun Int.dp(context: Context) = toFloat().dp(context)
