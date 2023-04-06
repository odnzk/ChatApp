package com.study.chat.presentation.chat.util.delegates.message

import androidx.recyclerview.widget.DiffUtil
import com.study.chat.presentation.chat.model.UiMessage
import com.study.components.recycler.delegates.Delegate

internal class MessageDelegate(
    onLongClickListener: ((Int) -> Unit)?,
    onAddReactionClickListener: ((Int) -> Unit)?,
    onReactionClick: ((messageId: Int, emojiName: String) -> Unit)?
) : Delegate<MessageViewHolder, UiMessage>(
    isType = { it is UiMessage },
    viewHolderCreator = {
        MessageViewHolder.create(
            parent = it,
            onLongClickListener = onLongClickListener,
            onAddReactionClickListener = onAddReactionClickListener,
            onReactionClick = onReactionClick
        )
    }, comparator = object : DiffUtil.ItemCallback<UiMessage>() {
        override fun areItemsTheSame(oldItem: UiMessage, newItem: UiMessage): Boolean {
            return oldItem.id == newItem.id && oldItem::class == newItem::class
        }

        override fun areContentsTheSame(oldItem: UiMessage, newItem: UiMessage): Boolean {
            return when (oldItem) {
                is UiMessage.ChatMessage -> {
                    (newItem as UiMessage.ChatMessage).let { newItem ->
                        oldItem.content == newItem.content
                                && oldItem.senderAvatarUrl == newItem.senderAvatarUrl
                                && oldItem.senderName == newItem.senderName
                                && oldItem.reactions == newItem.reactions
                    }
                }
                is UiMessage.MeMessage -> oldItem.content == newItem.content
            }
        }
    }, viewBinder = { viewHolder, item -> viewHolder.bind(item) })

