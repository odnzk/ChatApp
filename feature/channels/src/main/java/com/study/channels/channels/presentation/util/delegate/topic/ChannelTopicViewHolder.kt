package com.study.channels.channels.presentation.util.delegate.topic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.study.channels.R
import com.study.channels.channels.presentation.model.UiChannelTopic
import com.study.channels.databinding.ItemChannelTopicBinding

internal class ChannelTopicViewHolder(
    private val binding: ItemChannelTopicBinding,
    private val onTopicClick: ((UiChannelTopic) -> Unit)?
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(topic: UiChannelTopic) {
        with(binding) {
            itemChannelTopicTvName.text = root.context.getString(
                R.string.item_channel_topic_name, topic.title
            )
            root.setOnClickListener { onTopicClick?.invoke(topic) }
            topic.backgroundColor?.let { root.setBackgroundColor(it) }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onTopicClick: ((UiChannelTopic) -> Unit)?
        ): ChannelTopicViewHolder = ChannelTopicViewHolder(
            ItemChannelTopicBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onTopicClick = onTopicClick
        )
    }
}
