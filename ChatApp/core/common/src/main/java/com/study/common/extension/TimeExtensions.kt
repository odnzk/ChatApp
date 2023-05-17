package com.study.common.extension

import java.util.Calendar

private const val UNIX_CONSTANT = 1000L
fun Int.unixToCalendar(): Calendar =
    Calendar.getInstance().apply { timeInMillis = this@unixToCalendar * UNIX_CONSTANT }

fun Calendar.isSameDay(other: Calendar): Boolean =
    get(Calendar.DATE) == other.get(Calendar.DATE)
            && get(Calendar.MONTH) == other.get(Calendar.MONTH)
            && get(Calendar.YEAR) == other.get(Calendar.YEAR)

