package com.study.chat.presentation.chat.util.mapper

import android.content.Context
import com.study.chat.R
import java.util.Calendar
import java.util.Locale

fun Calendar.toDateSeparatorString(context: Context) =
    context.getString(
        R.string.date_delegate_item_title_format,
        get(Calendar.DAY_OF_MONTH),
        getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
            ?.take(DEFAULT_MONTH_NAME_LENGTH)
    )

private const val DEFAULT_MONTH_NAME_LENGTH = 3
