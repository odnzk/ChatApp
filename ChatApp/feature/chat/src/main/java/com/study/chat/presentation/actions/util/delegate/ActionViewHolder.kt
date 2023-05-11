package com.study.chat.presentation.actions.util.delegate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.study.chat.databinding.ItemActionBinding
import com.study.chat.presentation.actions.util.model.UiAction

internal class ActionViewHolder(
    private val binding: ItemActionBinding,
    private val onActionClick: ((UiAction) -> Unit)?
) :
    RecyclerView.ViewHolder(binding.root) {

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
