package com.study.components.recycler.delegates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.study.components.databinding.ItemProgressBinding

/**
 * A [RecyclerView.ViewHolder] for the loading state when pagination is being performed.
 *
 * @property binding The [ItemProgressBinding] instance for this [RecyclerView.ViewHolder].
 */
class LoadingViewHolder(binding: ItemProgressBinding) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun create(
            parent: ViewGroup
        ): LoadingViewHolder = LoadingViewHolder(
            ItemProgressBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}
