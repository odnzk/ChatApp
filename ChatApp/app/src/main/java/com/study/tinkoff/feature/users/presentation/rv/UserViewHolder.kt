package com.study.tinkoff.feature.users.presentation.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.study.common.extensions.loadUrl
import com.study.components.view.AvatarImageView
import com.study.components.view.AvatarImageView.Status
import com.study.domain.model.User
import com.study.tinkoff.databinding.ItemUserBinding

class UserViewHolder(
    private val binding: ItemUserBinding, private val onUserClick: ((userId: Int) -> Unit)? = null
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(user: User) {
        with(binding) {
            itemUserIvAvatar.loadUrl(user.avatarUrl)
            itemUserTvEmail.text = user.email
            itemUserTvUsername.text = user.name
            (itemUserIvAvatar as AvatarImageView).status =
                if (user.is_active) Status.ONLINE else Status.OFFLINE
            root.setOnClickListener { onUserClick?.invoke(user.id) }
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
