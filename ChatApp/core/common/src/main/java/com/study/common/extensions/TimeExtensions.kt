package com.study.common.extensions

import java.util.Calendar

fun Int.toCalendar(): Calendar =
    Calendar.getInstance().apply { timeInMillis = this@toCalendar.toLong() }
