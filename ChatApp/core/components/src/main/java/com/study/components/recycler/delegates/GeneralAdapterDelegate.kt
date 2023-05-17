package com.study.components.recycler.delegates

import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * A generic adapter delegate to support binding data to a variety of view types.
 *
 * @param delegates a list of [Delegate] objects to bind data to the target views.
 */
class GeneralAdapterDelegate(private val delegates: List<Delegate<RecyclerView.ViewHolder, Any>>) :
    ListAdapter<Any, RecyclerView.ViewHolder>(GeneralItemCallback(delegates)) {

    /**
     * Returns the view type that corresponds to the data at the specified [position].
     */
    override fun getItemViewType(position: Int): Int {
        val currentItem = getItem(position)
        return delegates.indexOfFirst { it.isType(currentItem) }
    }

    /**
     * Creates a new [RecyclerView.ViewHolder] instance for the item specified by [viewType].
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        delegates[viewType].viewHolderCreator(parent)


    /**
     * Binds the data at the specified [position] to the specified [holder].
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        delegates[getItemViewType(position)].viewBinder(holder, getItem(position))

    /**
     * Binds the data at the specified [position] to the specified [holder], with the given [payloads].
     */
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
    override fun areItemsTheSame(oldItem: Any, newItem: Any) = oldItem::class == newItem::class
            && delegates.first { it.isType(oldItem) }.comparator.areItemsTheSame(newItem, oldItem)

    override fun areContentsTheSame(oldItem: Any, newItem: Any) =
        delegates
            .find { it.isType(oldItem) }
            ?.comparator?.areContentsTheSame(newItem, oldItem) == true

    override fun getChangePayload(oldItem: Any, newItem: Any): Any? =
        delegates.find { it.isType(oldItem) }?.comparator?.getChangePayload(oldItem, newItem)
}
