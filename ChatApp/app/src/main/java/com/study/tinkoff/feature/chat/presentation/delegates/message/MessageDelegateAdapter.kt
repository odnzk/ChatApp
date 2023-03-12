package com.study.tinkoff.feature.chat.presentation.delegates.message

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.study.tinkoff.core.ui.rv.delegates.AdapterDelegate
import com.study.tinkoff.core.ui.rv.delegates.DelegateItem

class MessageDelegateAdapter : AdapterDelegate<MessageDelegateItem> {
    var onLongClickListener: ((Int) -> Unit)? = null
    var onAddReactionClickListener: ((Int) -> Unit)? = null
    var onReactionClick: ((messageId: Int, emojiName: String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return MessageViewHolder.create(
            parent,
            onLongClickListener = onLongClickListener,
            onAddReactionClickListener = onAddReactionClickListener,
            onReactionClick = onReactionClick
        )
    }

    override fun isOfViewType(item: DelegateItem<*>): Boolean {
        return item is MessageDelegateItem
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: MessageDelegateItem,
        position: Int
    ) {
        (holder as MessageViewHolder).bind(item.content())
    }

}
