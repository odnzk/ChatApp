package com.study.components.recycler.delegates

import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class GeneralAdapterDelegate(private val delegates: List<Delegate<RecyclerView.ViewHolder, Any>>) :
    ListAdapter<Any, RecyclerView.ViewHolder>(GeneralItemCallback(delegates)) {

    override fun getItemViewType(position: Int): Int {
        val currentItem = getItem(position)
        return delegates.indexOfFirst { it.isType(currentItem) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegates[viewType].viewHolderCreator(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegates[getItemViewType(position)].viewBinder(holder, getItem(position))
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            payloads.last().takeIf { it is Bundle }?.let {
                delegates[getItemViewType(position)].viewBinderWithPayloads?.invoke(
                    holder,
                    getItem(position),
                    it
                )
            }
        }
    }
}

internal class GeneralItemCallback(private val delegates: List<Delegate<RecyclerView.ViewHolder, Any>>) :
    DiffUtil.ItemCallback<Any>() {
    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        return oldItem::class == newItem::class && delegates.first { it.isType(oldItem) }.comparator.areItemsTheSame(
            newItem, oldItem
        )
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        return delegates.find { it.isType(oldItem) }?.comparator?.areContentsTheSame(
            newItem, oldItem
        ) == true
    }

    override fun getChangePayload(oldItem: Any, newItem: Any): Any? {
        return delegates.find { it.isType(oldItem) }?.comparator?.getChangePayload(oldItem, newItem)
    }
}
