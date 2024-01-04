package com.study.chat.actions.presentation.util.delegate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.study.chat.actions.presentation.model.UiAction
import com.study.chat.databinding.ItemActionBinding

internal class ActionViewHolder(
    private val binding: ItemActionBinding,
    private val onActionClick: ((UiAction) -> Unit)?
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(action: UiAction) {
        with(binding) {
            itemActionTvAction.text = root.context.getString(action.titleRes)
            itemActionTvAction.setCompoundDrawablesWithIntrinsicBounds(
                ContextCompat.getDrawable(root.context, action.iconRes),
                null,
                null,
                null
            )
            root.setOnClickListener { onActionClick?.invoke(action) }
        }
    }

    companion object {
        fun create(parent: ViewGroup, onActionClick: ((UiAction) -> Unit)?) = ActionViewHolder(
            ItemActionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onActionClick
        )
    }
}
