package com.study.chat.presentation.actions.util.delegate

import androidx.recyclerview.widget.DiffUtil.ItemCallback
import com.study.chat.presentation.actions.util.model.UiAction
import com.study.components.recycler.delegates.Delegate

internal class ActionDelegate(private val onActionClick: ((UiAction) -> Unit)?) :
    Delegate<ActionViewHolder, UiAction>(
        isType = { it is UiAction },
        viewHolderCreator = { ActionViewHolder.create(it, onActionClick) },
        viewBinder = { holder, item -> holder.bind(item) },
        comparator = object : ItemCallback<UiAction>() {
            override fun areItemsTheSame(oldItem: UiAction, newItem: UiAction): Boolean =
                oldItem.titleRes == newItem.titleRes

            override fun areContentsTheSame(oldItem: UiAction, newItem: UiAction): Boolean =
                oldItem.iconRes == newItem.iconRes && newItem.titleRes == oldItem.titleRes

        }
    )
