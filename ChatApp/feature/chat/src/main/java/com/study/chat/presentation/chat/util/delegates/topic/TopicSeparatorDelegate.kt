package com.study.chat.presentation.chat.util.delegates.topic

import androidx.recyclerview.widget.DiffUtil
import com.study.chat.presentation.chat.util.model.TopicSeparator
import com.study.components.recycler.delegates.Delegate

internal class TopicSeparatorDelegate : Delegate<TopicSeparatorViewHolder, TopicSeparator>(
    isType = { it is TopicSeparator },
    viewHolderCreator = { TopicSeparatorViewHolder.create(it) },
    viewBinder = { holder, topic -> holder.bind(topic) },
    comparator = object : DiffUtil.ItemCallback<TopicSeparator>() {
        override fun areItemsTheSame(oldItem: TopicSeparator, newItem: TopicSeparator): Boolean =
            oldItem.topicTitle == newItem.topicTitle && oldItem.calendar == newItem.calendar

        override fun areContentsTheSame(oldItem: TopicSeparator, newItem: TopicSeparator): Boolean =
            oldItem.topicTitle == newItem.topicTitle && oldItem.calendar == newItem.calendar
    }
)
