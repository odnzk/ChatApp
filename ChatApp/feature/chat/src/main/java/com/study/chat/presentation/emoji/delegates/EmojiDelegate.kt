package com.study.chat.presentation.emoji.delegates

import androidx.recyclerview.widget.DiffUtil
import com.study.chat.domain.model.Emoji
import com.study.components.recycler.delegates.Delegate

internal class EmojiDelegate(onEmojiClick: (emoji: Emoji) -> Unit) :
    Delegate<EmojiViewHolder, Emoji>(
        isType = { it is Emoji },
        viewHolderCreator = { EmojiViewHolder.create(it, onEmojiClick) },
        comparator = object : DiffUtil.ItemCallback<Emoji>() {
            override fun areItemsTheSame(oldItem: Emoji, newItem: Emoji): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Emoji, newItem: Emoji): Boolean {
                return oldItem == newItem
            }

        },
        viewBinder = { viewHolder, item -> viewHolder.bind(item) })
