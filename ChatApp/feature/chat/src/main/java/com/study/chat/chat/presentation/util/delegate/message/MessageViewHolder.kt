package com.study.chat.chat.presentation.util.delegate.message

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.recyclerview.widget.RecyclerView
import com.study.chat.chat.presentation.model.UiEmoji
import com.study.chat.chat.presentation.model.UiMessage
import com.study.chat.chat.presentation.util.view.MessageView

internal class MessageViewHolder(
    private val view: MessageView,
    private val onLongClickListener: ((message: UiMessage) -> Unit)? = null,
    private val onAddReactionClick: ((message: UiMessage) -> Unit)? = null,
    private val onReactionClick: ((message: UiMessage, emoji: UiEmoji) -> Unit)? = null
) : RecyclerView.ViewHolder(view) {
    fun bind(message: UiMessage) {
        with(view) {
            setMessage(message, onReactionClick)
            onAddReactionClickListener = View.OnClickListener {
                onAddReactionClick?.invoke(message)
            }
            setOnLongClickListener {
                onLongClickListener?.invoke(message)
                true
            }
        }
    }

    fun bindWithPayloads(message: UiMessage, payloads: Any) {
        if ((payloads as Bundle).getBoolean(PAYLOAD_EMOJI_LIST_CHANGED)) {
            view.addReactions(message.reactions, message, onReactionClick)
        }
    }

    companion object {
        internal const val PAYLOAD_EMOJI_LIST_CHANGED = "reactionsUpdate"
        private const val MAX_MESSAGE_WIDTH_PERCENT = 0.8f
        fun create(
            parent: ViewGroup,
            onLongClickListener: ((message: UiMessage) -> Unit)? = null,
            onAddReactionClickListener: ((message: UiMessage) -> Unit)? = null,
            onReactionClick: ((message: UiMessage, emoji: UiEmoji) -> Unit)? = null
        ): MessageViewHolder {
            val itemView = MessageView(parent.context).apply {
                layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                maxWidthPercent = MAX_MESSAGE_WIDTH_PERCENT
            }
            return MessageViewHolder(
                itemView,
                onLongClickListener = onLongClickListener,
                onAddReactionClick = onAddReactionClickListener,
                onReactionClick = onReactionClick
            )
        }
    }
}
