package com.study.tinkoff.feature.channels.presentation.delegates.topic

import androidx.recyclerview.widget.DiffUtil.ItemCallback
import com.study.common.rv.delegates.Delegate
import com.study.tinkoff.feature.channels.presentation.model.UiChannelTopic

class ChannelTopicDelegate(onTopicClick: ((channelId: Int, lastMessageId: Int) -> Unit)?) :
    Delegate<ChannelTopicViewHolder, UiChannelTopic>(isType = { it is UiChannelTopic },
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
