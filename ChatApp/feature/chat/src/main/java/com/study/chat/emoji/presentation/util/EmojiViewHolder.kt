package com.study.chat.emoji.presentation.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.study.chat.chat.presentation.model.UiEmoji
import com.study.chat.databinding.ItemEmojiBinding
import com.study.chat.shared.presentation.util.toEmojiString

internal class EmojiViewHolder(
    private val binding: ItemEmojiBinding,
    private val onEmojiClickListener: ((emoji: UiEmoji) -> Unit)? = null
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(emoji: UiEmoji) {
        with(binding.root) {
            text = emoji.code.toEmojiString()
            setOnClickListener { onEmojiClickListener?.invoke(emoji) }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onEmojiClickListener: ((UiEmoji) -> Unit)? = null
        ): EmojiViewHolder = EmojiViewHolder(
            ItemEmojiBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onEmojiClickListener
        )
    }
}
