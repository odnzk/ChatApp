package com.study.tinkoff.feature.chat.presentation.delegates.message

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.study.tinkoff.core.ui.views.MessageView
import com.study.tinkoff.feature.chat.presentation.model.UiMessage

class MessageViewHolder(
    private val view: MessageView,
    private val onLongClickListener: ((Int) -> Unit)? = null,
    private val onAddReactionClickListener: ((Int) -> Unit)? = null,
    private val onReactionClick: ((messageId: Int, emojiName: String) -> Unit)? = null
) : RecyclerView.ViewHolder(view) {
    fun bind(message: UiMessage) {
        when (message) {
            is UiMessage.ChatMessage -> view.setChatMessage(
                messageId = message.id,
                avatarUrl = requireNotNull(message.senderAvatarUrl),
                senderName = message.senderName,
                messageContent = message.content,
                reactions = message.reactions,
                onReactionClick = onReactionClick
            )
            is UiMessage.MeMessage -> view.setUserMessage(
                messageId = message.id,
                messageContent = message.content,
                reactions = message.reactions,
                onReactionClick = onReactionClick
            )
        }
        view.onAddReactionClickListener = View.OnClickListener {
            onAddReactionClickListener?.invoke(message.id)
        }
        view.setOnLongClickListener {
            onLongClickListener?.invoke(message.id)
            true
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onLongClickListener: ((Int) -> Unit)? = null,
            onAddReactionClickListener: ((Int) -> Unit)? = null,
            onReactionClick: ((messageId: Int, emojiName: String) -> Unit)? = null
        ): MessageViewHolder {
            val itemView = MessageView(parent.context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
            return MessageViewHolder(
                itemView,
                onLongClickListener = onLongClickListener,
                onAddReactionClickListener = onAddReactionClickListener,
                onReactionClick = onReactionClick
            )
        }
    }
}
