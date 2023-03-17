package com.study.tinkoff.feature.chat.presentation.delegates.date

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.study.tinkoff.core.ui.rv.delegates.AdapterDelegate
import com.study.tinkoff.core.ui.rv.delegates.DelegateItem

class DateDelegateAdapter : AdapterDelegate<DateDelegateItem> {
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return DateViewHolder.create(parent)
    }

    override fun isOfViewType(item: DelegateItem<*>): Boolean {
        return item is DateDelegateItem
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: DateDelegateItem,
        position: Int
    ) {
        (holder as DateViewHolder).bind(item.content())
    }

}
