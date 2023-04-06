package com.study.channels.presentation.util.delegates.channel

import androidx.recyclerview.widget.DiffUtil.ItemCallback
import com.study.channels.presentation.model.UiChannel
import com.study.components.recycler.delegates.Delegate
import com.study.components.recycler.shimmer.ShimmerItem

internal class ChannelDelegate(onChannelClick: ((channelId: Int) -> Unit)?) :
    Delegate<ChannelViewHolder, ShimmerItem<UiChannel>>(isType = { it is ShimmerItem<*> },
        viewHolderCreator = { ChannelViewHolder.create(it, onChannelClick = onChannelClick) },
        viewBinder = { holder, channel ->
            holder.bind(channel)
        },
        comparator = object : ItemCallback<ShimmerItem<UiChannel>>() {
            override fun areItemsTheSame(
                oldItem: ShimmerItem<UiChannel>, newItem: ShimmerItem<UiChannel>
            ): Boolean {
                val oldContent = oldItem.content()
                val newContent = newItem.content()
                return when {
                    oldContent == null && newContent == null -> true
                    oldContent == null || newContent == null -> false
                    else -> oldContent.id == newContent.id
                }
            }

            override fun areContentsTheSame(
                oldItem: ShimmerItem<UiChannel>, newItem: ShimmerItem<UiChannel>
            ): Boolean {
                val oldContent = oldItem.content()
                val newContent = newItem.content()
                if (oldContent == null || newContent == null) return true
                return oldContent.title == newContent.title
                        && oldContent.isCollapsed == newContent.isCollapsed
            }
        })
