package com.study.chat.chat.presentation.util.delegate.topic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.study.chat.chat.presentation.model.TopicSeparator
import com.study.chat.chat.presentation.util.mapper.toDateSeparatorString
import com.study.chat.databinding.ItemTopicBinding

internal class TopicSeparatorViewHolder(
    private val binding: ItemTopicBinding,
    private val onTopicClick: ((title: String) -> Unit)?
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(topicSeparator: TopicSeparator) = with(binding) {
        itemTopicSeparatorTitle.text = topicSeparator.topicTitle
        itemTopicSeparatorDate.text = topicSeparator.calendar.toDateSeparatorString(root.context)
        root.setOnClickListener { onTopicClick?.invoke(topicSeparator.topicTitle) }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onTopicClick: ((title: String) -> Unit)?
        ): TopicSeparatorViewHolder =
            TopicSeparatorViewHolder(
                ItemTopicBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), onTopicClick
            )
    }
}
