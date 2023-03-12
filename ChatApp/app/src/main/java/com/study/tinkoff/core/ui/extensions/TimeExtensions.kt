package com.study.tinkoff.core.ui.extensions

import java.util.Calendar

fun Int.toCalendar(): Calendar =
    Calendar.getInstance().apply { timeInMillis = this@toCalendar.toLong() }
