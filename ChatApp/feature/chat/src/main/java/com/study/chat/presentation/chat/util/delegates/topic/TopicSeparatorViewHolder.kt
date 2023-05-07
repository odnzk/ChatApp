package com.study.chat.presentation.chat.util.delegates.topic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.study.chat.databinding.ItemTopicBinding
import com.study.chat.presentation.chat.util.mapper.toDateSeparatorString
import com.study.chat.presentation.chat.util.model.TopicSeparator

internal class TopicSeparatorViewHolder(private val binding: ItemTopicBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(topicSeparator: TopicSeparator) = with(binding) {
        itemTopicSeparatorTitle.text = topicSeparator.topicTitle
        itemTopicSeparatorDate.text = topicSeparator.calendar.toDateSeparatorString(root.context)
    }

    companion object {
        fun create(parent: ViewGroup): TopicSeparatorViewHolder =
            TopicSeparatorViewHolder(
                ItemTopicBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
    }
}
