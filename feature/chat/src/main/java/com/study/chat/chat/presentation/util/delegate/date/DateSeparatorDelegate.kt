package com.study.chat.chat.presentation.util.delegate.date

import androidx.recyclerview.widget.DiffUtil
import com.study.chat.chat.presentation.model.DateSeparator
import com.study.components.recycler.delegates.Delegate
import java.util.Calendar

internal class DateSeparatorDelegate : Delegate<DateSeparatorViewHolder, DateSeparator>(
    isType = { it is DateSeparator },
    viewHolderCreator = { DateSeparatorViewHolder.create(parent = it) },
    comparator = object : DiffUtil.ItemCallback<DateSeparator>() {
        override fun areItemsTheSame(oldItem: DateSeparator, newItem: DateSeparator): Boolean {
            return oldItem.calendar.timeInMillis == newItem.calendar.timeInMillis
        }

        override fun areContentsTheSame(oldItem: DateSeparator, newItem: DateSeparator): Boolean {
            val oldCalendar = oldItem.calendar
            val newCalendar = newItem.calendar
            return oldCalendar.get(Calendar.DATE) == newCalendar.get(Calendar.DATE)
                    && oldCalendar.get(Calendar.YEAR) == newCalendar.get(Calendar.YEAR)
                    && oldCalendar.get(Calendar.MONTH) == newCalendar.get(Calendar.MONTH)
        }
    },
    viewBinder = { viewHolder, item -> viewHolder.bind(item) })
