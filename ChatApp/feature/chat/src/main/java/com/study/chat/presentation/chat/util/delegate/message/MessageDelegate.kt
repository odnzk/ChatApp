package com.study.chat.presentation.chat.util.delegate.message

import androidx.core.os.bundleOf
import androidx.recyclerview.widget.DiffUtil
import com.study.chat.presentation.util.model.UiEmoji
import com.study.chat.presentation.chat.util.model.UiMessage
import com.study.components.recycler.delegates.Delegate

internal class MessageDelegate(
    onLongClickListener: ((message: UiMessage) -> Unit)?,
    onAddReactionClickListener: ((message: UiMessage) -> Unit)?,
    onReactionClick: ((message: UiMessage, emoji: UiEmoji) -> Unit)?
) : Delegate<MessageViewHolder, UiMessage>(
    isType = { it is UiMessage },
    viewHolderCreator = {
        MessageViewHolder.create(
            parent = it,
            onLongClickListener = onLongClickListener,
            onAddReactionClickListener = onAddReactionClickListener,
            onReactionClick = onReactionClick
        )
    },
    viewBinder = { viewHolder, item -> viewHolder.bind(item) },
    viewBinderWithPayloads = { holder, message, payloads ->
        holder.bindWithPayloads(
            message,
            payloads
        )
    },
    comparator = object : DiffUtil.ItemCallback<UiMessage>() {
        override fun areItemsTheSame(oldItem: UiMessage, newItem: UiMessage): Boolean {
            return oldItem.id == newItem.id && oldItem::class == newItem::class
        }

        override fun areContentsTheSame(oldItem: UiMessage, newItem: UiMessage): Boolean {
            return when (oldItem) {
                is UiMessage.ChatMessage -> {
                    (newItem as UiMessage.ChatMessage).let { new ->
                        oldItem.content == new.content
                                && oldItem.senderAvatarUrl == new.senderAvatarUrl
                                && oldItem.senderName == new.senderName
                                && oldItem.reactions == new.reactions
                    }
                }
                is UiMessage.MeMessage -> oldItem.content == newItem.content
            }
        }

        override fun getChangePayload(oldItem: UiMessage, newItem: UiMessage): Any? {
            return if (oldItem.reactions != newItem.reactions) {
                bundleOf(MessageViewHolder.PAYLOAD_EMOJI_LIST_CHANGED to true)
            } else null
        }
    })

