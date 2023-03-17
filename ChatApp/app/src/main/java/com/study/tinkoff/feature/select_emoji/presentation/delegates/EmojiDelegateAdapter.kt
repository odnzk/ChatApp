package com.study.tinkoff.feature.select_emoji.presentation.delegates

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.study.tinkoff.core.ui.rv.delegates.AdapterDelegate
import com.study.tinkoff.core.ui.rv.delegates.DelegateItem

class EmojiDelegateAdapter : AdapterDelegate<EmojiDelegateItem> {
    var onEmojiClickListener: ((emojiName: String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return EmojiViewHolder.create(parent, onEmojiClickListener = onEmojiClickListener)
    }

    override fun isOfViewType(item: DelegateItem<*>): Boolean {
        return item is EmojiDelegateItem
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: EmojiDelegateItem,
        position: Int
    ) {
        (holder as EmojiViewHolder).bind(item.content())
    }
}
