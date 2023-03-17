package com.study.tinkoff.feature.chat.presentation.delegates.message

import com.study.tinkoff.core.ui.rv.delegates.DelegateItem
import com.study.tinkoff.feature.chat.presentation.delegates.date.DateDelegateItem
import com.study.tinkoff.feature.chat.presentation.model.UiMessage
import java.util.Calendar

private fun MutableList<DelegateItem<Any>>.safeAdd(item: DelegateItem<*>) {
    (item as? DelegateItem<Any>)?.let { add(it) }
}

fun List<UiMessage>.toMessageDelegateItemsWithDateGrouping(): List<DelegateItem<Any>> {
    if (isEmpty()) return emptyList()
    val resultList = mutableListOf<DelegateItem<Any>>()
    var lastAddedDate: Calendar = Calendar.getInstance().apply { timeInMillis = 0 }
    for (message in sortedBy { it.calendar.time }) {
        if (lastAddedDate.get(Calendar.DATE) != message.calendar.get(Calendar.DATE)
            || lastAddedDate.get(Calendar.MONTH) != message.calendar.get(Calendar.MONTH)
            || lastAddedDate.get(Calendar.YEAR) != message.calendar.get(
                Calendar.YEAR
            )
        ) {
            lastAddedDate = message.calendar
            resultList.safeAdd(DateDelegateItem(lastAddedDate))
        }
        resultList.safeAdd(MessageDelegateItem(message))
    }
    return resultList
}

