package com.study.chat.presentation.chat.delegates.message

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.study.chat.presentation.chat.model.UiMessage
import com.study.chat.presentation.chat.view.MessageView

internal class MessageViewHolder(
    private val view: MessageView,
    private val onLongClickListener: ((Int) -> Unit)? = null,
    private val onAddReactionClickListener: ((Int) -> Unit)? = null,
    private val onReactionClick: ((messageId: Int, emojiName: String) -> Unit)? = null
) : RecyclerView.ViewHolder(view) {
    fun bind(message: UiMessage) {
        view.setMessage(message, onReactionClick)
        view.onAddReactionClickListener = View.OnClickListener {
            onAddReactionClickListener?.invoke(message.id)
        }
        view.setOnLongClickListener {
            onLongClickListener?.invoke(message.id)
            true
        }
    }

    companion object {
        private const val MAX_MESSAGE_WIDTH_PERCENT = 0.7f
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
                maxWidthPercent = MAX_MESSAGE_WIDTH_PERCENT
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
