package com.study.channels.presentation.channels.util.delegate.topic

import androidx.recyclerview.widget.DiffUtil.ItemCallback
import com.study.channels.presentation.channels.model.UiChannelTopic
import com.study.components.recycler.delegates.Delegate

internal class ChannelTopicDelegate(onTopicClick: ((UiChannelTopic) -> Unit)?) :
    Delegate<ChannelTopicViewHolder, UiChannelTopic>(
        isType = { it is UiChannelTopic },
        viewHolderCreator = { ChannelTopicViewHolder.create(it, onTopicClick = onTopicClick) },
        viewBinder = { holder, topic -> holder.bind(topic) },
        comparator = object : ItemCallback<UiChannelTopic>() {
            override fun areItemsTheSame(
                oldItem: UiChannelTopic, newItem: UiChannelTopic
            ): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(
                oldItem: UiChannelTopic, newItem: UiChannelTopic
            ): Boolean {
                return oldItem == newItem
            }
        }
    )
