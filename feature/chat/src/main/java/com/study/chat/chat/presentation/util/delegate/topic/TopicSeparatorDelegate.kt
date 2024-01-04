package com.study.chat.chat.presentation.util.delegate.topic

import androidx.recyclerview.widget.DiffUtil
import com.study.chat.chat.presentation.model.TopicSeparator
import com.study.components.recycler.delegates.Delegate

internal class TopicSeparatorDelegate(onTopicClick: ((title: String) -> Unit)?) :
    Delegate<TopicSeparatorViewHolder, TopicSeparator>(
        isType = { it is TopicSeparator },
        viewHolderCreator = { TopicSeparatorViewHolder.create(it, onTopicClick) },
        viewBinder = { holder, topic -> holder.bind(topic) },
        comparator = object : DiffUtil.ItemCallback<TopicSeparator>() {
            override fun areItemsTheSame(
                oldItem: TopicSeparator,
                newItem: TopicSeparator
            ): Boolean =
                oldItem.topicTitle == newItem.topicTitle && oldItem.calendar == newItem.calendar

            override fun areContentsTheSame(
                oldItem: TopicSeparator,
                newItem: TopicSeparator
            ): Boolean =
                oldItem.topicTitle == newItem.topicTitle && oldItem.calendar == newItem.calendar
        }
    )
