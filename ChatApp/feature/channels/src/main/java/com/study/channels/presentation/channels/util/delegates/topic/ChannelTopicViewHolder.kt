package com.study.channels.presentation.channels.util.delegates.topic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.study.channels.R
import com.study.channels.databinding.ItemChannelTopicBinding
import com.study.channels.presentation.channels.util.model.UiChannelTopic

internal class ChannelTopicViewHolder(
    private val binding: ItemChannelTopicBinding,
    private val onTopicClick: ((channelId: Int, topicTitle: String) -> Unit)?
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(topic: UiChannelTopic) {
        with(binding) {
            itemChannelTopicTvMessagesCount.text = root.context.getString(
                R.string.item_channel_topic_messages_count, topic.messagesCount
            )
            itemChannelTopicTvName.text = root.context.getString(
                R.string.item_channel_topic_name, topic.title
            )
            root.setOnClickListener { onTopicClick?.invoke(topic.channelId, topic.title) }
            root.setBackgroundColor(topic.backgroundColor)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onTopicClick: ((channelId: Int, topicTitle: String) -> Unit)?
        ): ChannelTopicViewHolder = ChannelTopicViewHolder(
            ItemChannelTopicBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onTopicClick = onTopicClick
        )
    }
}
