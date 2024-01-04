package com.study.profile.presentation.util.delegate

import androidx.recyclerview.widget.DiffUtil
import com.study.components.recycler.delegates.Delegate

internal class RoleDelegate : Delegate<RoleViewHolder, Int>(
    isType = { it is Int },
    viewHolderCreator = { RoleViewHolder.create(it) },
    comparator = object : DiffUtil.ItemCallback<Int>() {
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }

    },
    viewBinder = { viewHolder: RoleViewHolder, item: Int -> viewHolder.bind(item) }
)
