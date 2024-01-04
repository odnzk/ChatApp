package com.study.chat.chat.presentation.util.delegate.date

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.study.chat.chat.presentation.model.DateSeparator
import com.study.chat.chat.presentation.util.mapper.toDateSeparatorString
import com.study.chat.databinding.ItemDateBinding

internal class DateSeparatorViewHolder(private val binding: ItemDateBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(dateSeparator: DateSeparator) = with(binding) {
        dateItemTitle.text = dateSeparator.calendar.toDateSeparatorString(root.context)
    }

    companion object {
        fun create(parent: ViewGroup): DateSeparatorViewHolder {
            return DateSeparatorViewHolder(
                ItemDateBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }
}
