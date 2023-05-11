package com.study.chat.presentation.emoji.delegate

import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.study.chat.presentation.util.model.UiEmoji
import com.study.chat.presentation.util.toEmojiString
import com.study.ui.R

internal class EmojiViewHolder(
    private val emojiTextView: TextView,
    private val onEmojiClickListener: ((emoji: UiEmoji) -> Unit)? = null
) : RecyclerView.ViewHolder(emojiTextView) {

    fun bind(emoji: UiEmoji) {
        emojiTextView.run {
            text = emoji.code.toEmojiString()
            setOnClickListener { onEmojiClickListener?.invoke(emoji) }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onEmojiClickListener: ((UiEmoji) -> Unit)? = null
        ): EmojiViewHolder {
            val itemView = TextView(parent.context).apply {
                layoutParams = ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
                setTextAppearance(R.style.TextAppearance_ChatApp_Title_Large)
            }
            return EmojiViewHolder(itemView, onEmojiClickListener)
        }
    }
}
