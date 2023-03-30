package com.study.channels.presentation.delegates.topic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.study.channels.databinding.ItemChannelTopicBinding
import com.study.channels.presentation.model.UiChannelTopic
import com.study.ui.R

internal class ChannelTopicViewHolder(
    private val binding: ItemChannelTopicBinding,
    private val onTopicClick: ((channelId: Int, lastMessageId: Int) -> Unit)?
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(topic: UiChannelTopic) {
        with(binding) {
            itemChannelTopicTvMessagesCount.text = root.context.getString(
                R.string.item_channel_topic_messages_count, topic.messagesCount
            )
            itemChannelTopicTvTopicName.text = topic.title
            root.setOnClickListener { onTopicClick?.invoke(topic.channelId, topic.lastMessageId) }
            root.setBackgroundColor(topic.backgroundColor)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onTopicClick: ((channelId: Int, lastMessageId: Int) -> Unit)?
        ): ChannelTopicViewHolder = ChannelTopicViewHolder(
            ItemChannelTopicBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onTopicClick = onTopicClick
        )
    }
}
