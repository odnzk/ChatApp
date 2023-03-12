package com.study.tinkoff.feature.select_emoji.presentation.delegates

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.study.tinkoff.R
import com.study.tinkoff.core.domain.model.Emoji
import com.study.tinkoff.core.ui.extensions.toEmojiString

class EmojiViewHolder(
    private val emojiTextView: TextView,
    private val onEmojiClickListener: ((emojiName: String) -> Unit)? = null
) : RecyclerView.ViewHolder(emojiTextView) {

    fun bind(emoji: Emoji) {
        emojiTextView.run {
            text = emoji.code.toEmojiString()
            setOnClickListener {
                onEmojiClickListener?.invoke(emoji.name)
            }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup, onEmojiClickListener: ((String) -> Unit)? = null
        ): EmojiViewHolder {
            val itemView = TextView(parent.context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                )
                setTextAppearance(R.style.TextAppearance_Messenger_Title_Large)
            }
            return EmojiViewHolder(
                itemView, onEmojiClickListener = onEmojiClickListener
            )
        }
    }
}
