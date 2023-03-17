package com.study.tinkoff.core.ui.rv.delegates

import androidx.recyclerview.widget.DiffUtil

class DelegateAdapterItemCallback<T : Any> : DiffUtil.ItemCallback<DelegateItem<T>>() {
    override fun areItemsTheSame(oldItem: DelegateItem<T>, newItem: DelegateItem<T>): Boolean {
        return oldItem::class == newItem::class && oldItem.id() == newItem.id()
    }

    override fun areContentsTheSame(oldItem: DelegateItem<T>, newItem: DelegateItem<T>): Boolean {
        return oldItem.compareToOther(newItem.content())
    }
}
