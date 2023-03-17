package com.study.tinkoff.core.ui.rv.delegates

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

interface AdapterDelegate<T : DelegateItem<*>> {
    fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
    fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: T, position: Int)
    fun isOfViewType(item: DelegateItem<*>): Boolean
}
