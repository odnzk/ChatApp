package com.study.tinkoff.feature.channels.presentation.delegates.channel

import androidx.recyclerview.widget.DiffUtil.ItemCallback
import com.study.common.rv.delegates.Delegate
import com.study.tinkoff.feature.channels.presentation.model.UiChannel

class ChannelDelegate(onChannelClick: ((channelId: Int) -> Unit)?) :
    Delegate<ChannelViewHolder, UiChannel>(isType = { it is UiChannel },
        viewHolderCreator = { ChannelViewHolder.create(it, onChannelClick = onChannelClick) },
        viewBinder = { holder, channel -> holder.bind(channel) },
        comparator = object : ItemCallback<UiChannel>() {
            override fun areItemsTheSame(oldItem: UiChannel, newItem: UiChannel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: UiChannel, newItem: UiChannel): Boolean {
                return oldItem.title == newItem.title && oldItem.isCollapsed == newItem.isCollapsed
            }
        }
    )
