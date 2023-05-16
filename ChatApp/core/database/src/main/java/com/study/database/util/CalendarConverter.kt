package com.study.database.util

import androidx.room.TypeConverter
import java.util.Calendar

internal class CalendarConverter {

    @TypeConverter
    fun toMillis(calendar: Calendar): Long = calendar.timeInMillis

    @TypeConverter
    fun fromMillis(timeMillis: Long): Calendar =
        Calendar.getInstance().apply { timeInMillis = timeMillis }

}
