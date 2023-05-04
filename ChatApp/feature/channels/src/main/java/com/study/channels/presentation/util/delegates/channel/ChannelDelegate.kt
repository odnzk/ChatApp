package com.study.channels.presentation.util.delegates.channel

import androidx.core.os.bundleOf
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import com.study.channels.presentation.util.model.UiChannel
import com.study.components.recycler.delegates.Delegate
import com.study.components.recycler.shimmer.ShimmerItem

internal class ChannelDelegate(onChannelClick: ((channelId: Int, isCollapsed: Boolean) -> Unit)?) :
    Delegate<ChannelViewHolder, ShimmerItem<UiChannel>>(
        isType = { it is ShimmerItem<*> },
        viewHolderCreator = { ChannelViewHolder.create(it, onChannelClick = onChannelClick) },
        viewBinder = { holder, channel -> holder.bind(channel) },
        viewBinderWithPayloads = { holder, channel, payloads ->
            holder.bindWithPayloads(channel, payloads)
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
                return oldContent == newContent
            }

            override fun getChangePayload(
                oldItem: ShimmerItem<UiChannel>,
                newItem: ShimmerItem<UiChannel>
            ): Any? {
                val oldContent = oldItem.content()
                val newContent = newItem.content()
                if (oldContent == null || newContent == null) return null
                return if (oldContent.isCollapsed != newContent.isCollapsed) {
                    bundleOf(ChannelViewHolder.PAYLOAD_IS_COLLAPSED_CHANGED to true)
                } else null
            }
        })
