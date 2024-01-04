package com.study.tinkoff.util

import androidx.room.TypeConverter
import java.util.Calendar

class CalendarConverter {

    @TypeConverter
    fun toMillis(calendar: Calendar): Long = calendar.timeInMillis

    @TypeConverter
    fun fromMillis(timeMillis: Long): Calendar =
        Calendar.getInstance().apply { timeInMillis = timeMillis }

}
