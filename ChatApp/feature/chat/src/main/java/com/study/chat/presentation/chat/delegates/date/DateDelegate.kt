package com.study.chat.presentation.chat.delegates.date

import androidx.recyclerview.widget.DiffUtil
import com.study.common.rv.delegates.Delegate
import java.util.Calendar

internal class DateDelegate : Delegate<DateViewHolder, Calendar>(isType = { it is Calendar },
    viewHolderCreator = { DateViewHolder.create(parent = it) },
    comparator = object : DiffUtil.ItemCallback<Calendar>() {
        override fun areItemsTheSame(oldItem: Calendar, newItem: Calendar): Boolean {
            return oldItem.timeInMillis == newItem.timeInMillis
        }

        override fun areContentsTheSame(oldItem: Calendar, newItem: Calendar): Boolean {
            return oldItem.get(Calendar.DATE) == newItem.get(Calendar.DATE)
                    && oldItem.get(Calendar.YEAR) == newItem.get(Calendar.YEAR)
                    && oldItem.get(Calendar.MONTH) == newItem.get(Calendar.MONTH)
        }
    },
    viewBinder = { viewHolder, item -> viewHolder.bind(item) })
