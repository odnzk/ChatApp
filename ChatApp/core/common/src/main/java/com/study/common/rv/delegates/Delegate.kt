package com.study.common.rv.delegates

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

open class Delegate<in VH : RecyclerView.ViewHolder, D : Any>(
    val isType: (Any) -> Boolean,
    val viewHolderCreator: (ViewGroup) -> RecyclerView.ViewHolder,
    val viewBinder: (VH, D) -> Unit,
    val comparator: DiffUtil.ItemCallback<D>
)
