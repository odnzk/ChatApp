package com.study.users.presentation.util.delegates

import androidx.recyclerview.widget.DiffUtil.ItemCallback
import com.study.components.recycler.delegates.Delegate
import com.study.components.recycler.shimmer.ShimmerItem
import com.study.users.presentation.model.UiUser

internal class UserDelegate(onUserClick: ((userId: Int) -> Unit)?) :
    Delegate<UserViewHolder, ShimmerItem<UiUser>>
        (
        isType = { it is ShimmerItem<*> },
        viewHolderCreator = { UserViewHolder.create(it, onUserClick = onUserClick) },
        viewBinder = { holder, channel -> holder.bind(channel) },
        comparator = object : ItemCallback<ShimmerItem<UiUser>>() {
            override fun areItemsTheSame(
                oldItem: ShimmerItem<UiUser>, newItem: ShimmerItem<UiUser>
            ): Boolean {
                val oldContent = oldItem.content()
                val newContent = newItem.content()
                if (oldContent == null || newContent == null) return true
                return oldContent.id == newContent.id
            }

            override fun areContentsTheSame(
                oldItem: ShimmerItem<UiUser>, newItem: ShimmerItem<UiUser>
            ): Boolean {
                val oldContent = oldItem.content()
                val newContent = newItem.content()
                if (oldContent == null || newContent == null) return true
                return oldContent == newContent
            }
        })

