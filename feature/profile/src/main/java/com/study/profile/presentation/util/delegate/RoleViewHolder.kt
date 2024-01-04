package com.study.profile.presentation.util.delegate

import android.content.res.ColorStateList
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.study.ui.R

internal class RoleViewHolder(private val chip: Chip) : RecyclerView.ViewHolder(chip) {

    fun bind(@StringRes role: Int) {
        chip.text = itemView.context.getString(role)
    }

    companion object {
        fun create(parent: ViewGroup): RoleViewHolder = RoleViewHolder(Chip(parent.context).apply {
            chipBackgroundColor =
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.purple_light))
            setTextColor(ContextCompat.getColor(context, R.color.white))
        })
    }
}
