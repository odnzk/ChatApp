package com.study.users.presentation.util.delegates

import android.view.LayoutInflater
import android.view.ViewGroup
import com.study.components.extensions.loadFromUrl
import com.study.components.recycler.shimmer.ShimmerItemViewHolder
import com.study.users.databinding.ItemUserBinding
import com.study.users.presentation.util.model.UiUser

internal class UserViewHolder(
    private val binding: ItemUserBinding,
    private val onUserClick: ((userId: Int) -> Unit)? = null
) : ShimmerItemViewHolder<UiUser>(binding) {

    override fun showContent(data: UiUser) {
        with(binding) {
            itemUserIvAvatar.loadFromUrl(data.avatarUrl)
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
