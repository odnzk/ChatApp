package com.study.chat.presentation.emoji.delegate

import androidx.recyclerview.widget.DiffUtil
import com.study.chat.presentation.util.model.UiEmoji
import com.study.components.recycler.delegates.Delegate

internal class EmojiDelegate(onEmojiClick: (emoji: UiEmoji) -> Unit) :
    Delegate<EmojiViewHolder, UiEmoji>(
        isType = { it is UiEmoji },
        viewHolderCreator = { EmojiViewHolder.create(it, onEmojiClick) },
        comparator = object : DiffUtil.ItemCallback<UiEmoji>() {
            override fun areItemsTheSame(oldItem: UiEmoji, newItem: UiEmoji): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: UiEmoji, newItem: UiEmoji): Boolean {
                return oldItem == newItem
            }

        },
        viewBinder = { viewHolder, item -> viewHolder.bind(item) })
