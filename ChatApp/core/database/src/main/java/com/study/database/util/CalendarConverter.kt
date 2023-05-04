package com.study.database.util

import androidx.room.TypeConverter
import java.util.Calendar

class CalendarConverter {

    @TypeConverter
    fun toMillis(calendar: Calendar): Long {
        return calendar.timeInMillis
    }

    @TypeConverter
    fun fromMillis(timeMillis: Long): Calendar {
        return Calendar.getInstance().apply { timeInMillis = timeMillis }
    }

}
