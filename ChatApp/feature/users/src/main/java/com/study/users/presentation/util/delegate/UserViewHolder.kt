package com.study.users.presentation.util.delegate

import android.view.LayoutInflater
import android.view.ViewGroup
import com.study.components.extension.loadFromUrl
import com.study.components.recycler.shimmer.ShimmerItemViewHolder
import com.study.users.databinding.ItemUserBinding
import com.study.users.presentation.model.UiUser
import com.study.ui.R as CoreR

internal class UserViewHolder(
    private val binding: ItemUserBinding,
    private val onUserClick: ((userId: Int) -> Unit)? = null
) : ShimmerItemViewHolder<UiUser>(binding) {

    override fun showContent(data: UiUser) {
        with(binding) {
            if (data.isBot && data.avatarUrl == null) {
                itemUserIvAvatar.setImageResource(CoreR.drawable.ic_bot)
            } else if (data.avatarUrl != null) {
                itemUserIvAvatar.loadFromUrl(data.avatarUrl)
            }
            itemUserTvEmail.text = data.email
            itemUserTvUsername.text = data.name
            itemUserIvAvatar.status = data.presence
            root.setOnClickListener { onUserClick?.invoke(data.id) }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup, onUserClick: ((userId: Int) -> Unit)? = null
        ): UserViewHolder = UserViewHolder(
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), onUserClick = onUserClick
        )
    }
}
