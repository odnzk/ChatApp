package com.study.users.presentation.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.study.common.extensions.loadUrl
import com.study.components.view.AvatarImageView.Status
import com.study.users.databinding.ItemUserBinding

internal class UserViewHolder(
    private val binding: ItemUserBinding, private val onUserClick: ((userId: Int) -> Unit)? = null
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(userState: UserState) {
        when (userState) {
            is UserState.Success -> {
                with(binding) {
                    itemUserShimmerLayout.hideShimmer()
                    itemUserIvAvatar.setBackgroundColor(itemView.context.getColor(com.study.ui.R.color.navy_light))
                    itemUserIvAvatar.loadUrl(userState.user.avatarUrl)
                    itemUserTvEmail.text = userState.user.email
                    itemUserTvUsername.text = userState.user.name
                    itemUserIvAvatar.status =
                        if (userState.user.isActive) Status.ONLINE else Status.OFFLINE
                    itemUserTvEmail.background = null
                    itemUserTvUsername.background = null
                    root.setOnClickListener { onUserClick?.invoke(userState.user.id) }
                }
            }
            UserState.Loading -> binding.itemUserShimmerLayout.startShimmer()
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
