package com.study.tinkoff.feature.users.presentation.rv

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import com.study.domain.model.User

class UsersAdapter : ListAdapter<User, UserViewHolder>(UserItemCallback) {
    var onUserClick: ((userId: Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder.create(parent, onUserClick)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

private val UserItemCallback = object : ItemCallback<User>() {
    override fun areItemsTheSame(
        oldItem: User,
        newItem: User
    ): Boolean {
        return newItem.id == oldItem.id
    }

    override fun areContentsTheSame(
        oldItem: User,
        newItem: User
    ): Boolean {
        return newItem.name == oldItem.name
                && newItem.email == oldItem.email
                && newItem.is_active == oldItem.is_active
                && newItem.avatarUrl == oldItem.avatarUrl
    }

}
