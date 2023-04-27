package com.study.database.converter

import androidx.room.TypeConverter
import java.util.Calendar

internal class CalendarConverter {

    @TypeConverter
    fun toMillis(calendar: Calendar): Long {
        return calendar.timeInMillis
    }

    @TypeConverter
    fun fromMillis(timeMillis: Long): Calendar {
        return Calendar.getInstance().apply { timeInMillis = timeMillis }
    }

}
