package com.study.components.recycler.delegates

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

open class Delegate<in ViewHolder : RecyclerView.ViewHolder, Delegate : Any>(
    val isType: (Any) -> Boolean,
    val viewHolderCreator: (ViewGroup) -> RecyclerView.ViewHolder,
    val viewBinder: (ViewHolder, Delegate) -> Unit,
    val comparator: DiffUtil.ItemCallback<Delegate>
)
