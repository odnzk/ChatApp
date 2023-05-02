package com.study.components.recycler.delegates

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

/**
 * A generic delegate class to bind data to a [RecyclerView.ViewHolder].
 *
 * @param isType a function to check if an item is of the relevant type to be bound by this delegate.
 * @param viewHolderCreator a function to create a new [RecyclerView.ViewHolder] instance for the delegate.
 * @param viewBinder a function to bind data to a [RecyclerView.ViewHolder] using a delegate.
 * @param comparator an implementation of [DiffUtil.ItemCallback] to provide item comparison.
 * @param viewBinderWithPayloads a function that can be used to bind data to a [RecyclerView.ViewHolder] with payloads.
 *
 * @param ViewHolder the type of `ViewHolder` this delegate will bind to.
 * @param Delegate the type of delegate used to bind the data.
 */
open class Delegate<in ViewHolder : RecyclerView.ViewHolder, Delegate : Any>(
    val isType: (Any) -> Boolean,
    val viewHolderCreator: (ViewGroup) -> RecyclerView.ViewHolder,
    val viewBinder: (ViewHolder, Delegate) -> Unit,
    val comparator: DiffUtil.ItemCallback<Delegate>,
    val viewBinderWithPayloads: ((ViewHolder, Delegate, Any) -> Unit)? = null
)
