package com.study.tinkoff.feature.emoji.presentation.delegates.emoji

import androidx.recyclerview.widget.DiffUtil
import com.study.common.rv.delegates.Delegate
import com.study.domain.model.Emoji

class EmojiDelegate(onEmojiClick: (emojiName: String) -> Unit) : Delegate<EmojiViewHolder, Emoji>(
    isType = { it is Emoji },
    viewHolderCreator = {
        EmojiViewHolder.create(
            parent = it,
            onEmojiClickListener = onEmojiClick
        )
    },
    comparator = object : DiffUtil.ItemCallback<Emoji>() {
        override fun areItemsTheSame(oldItem: Emoji, newItem: Emoji): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Emoji, newItem: Emoji): Boolean {
            return oldItem == newItem
        }

    },
    viewBinder = { viewHolder, item -> viewHolder.bind(item) })
