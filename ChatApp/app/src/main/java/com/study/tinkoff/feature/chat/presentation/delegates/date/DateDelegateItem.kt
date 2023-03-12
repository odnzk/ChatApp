package com.study.tinkoff.feature.chat.presentation.delegates.date

import com.study.tinkoff.core.ui.rv.delegates.DelegateItem
import java.util.Calendar

class DateDelegateItem(private val calendar: Calendar) : DelegateItem<Calendar> {
    override fun content(): Calendar = calendar

    override fun id(): Int = calendar.timeInMillis.toInt()

    override fun compareToOther(other: Calendar): Boolean =
        calendar.get(Calendar.YEAR) == other.get(Calendar.YEAR)
                && calendar.get(Calendar.MONTH) == other.get(Calendar.MONTH)
                && calendar.get(Calendar.DATE) == other.get(Calendar.DATE)


}
