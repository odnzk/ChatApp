package com.study.chat.presentation.chat.util.delegates.date

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.study.chat.databinding.ItemDateBinding
import com.study.ui.R
import java.util.Calendar
import java.util.Locale

internal class DateViewHolder(private val binding: ItemDateBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(calendar: Calendar) {
        with(binding) {
            val month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
                ?.take(MONTH_NAME_LENGTH)
            root.context.getString(
                R.string.date_delegate_item_title_format, calendar.get(Calendar.DAY_OF_MONTH), month
            ).also { formattedDate ->
                dateItemTitle.text = formattedDate
            }
        }
    }

    companion object {
        private const val MONTH_NAME_LENGTH = 3
        fun create(parent: ViewGroup): DateViewHolder {
            return DateViewHolder(
                ItemDateBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }
}
