package com.study.users.presentation.rv

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import com.study.users.domain.model.User

internal class UsersAdapter : ListAdapter<UserState, UserViewHolder>(UserItemCallback) {
    var onUserClick: ((userId: Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder.create(parent, onUserClick)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

sealed interface UserState {
    object Loading : UserState
    class Success(val user: User) : UserState
}

private val UserItemCallback = object : ItemCallback<UserState>() {
    override fun areItemsTheSame(oldItem: UserState, newItem: UserState): Boolean {
        return oldItem::class == newItem::class
                && if (oldItem is UserState.Success) oldItem.user.id == (newItem as UserState.Success).user.id
        else true
    }

    override fun areContentsTheSame(oldItem: UserState, newItem: UserState): Boolean {
        return if (oldItem is UserState.Success && newItem is UserState.Success) {
            newItem.user.name == oldItem.user.name
                    && newItem.user.email == oldItem.user.email
                    && newItem.user.isActive == oldItem.user.isActive
                    && newItem.user.avatarUrl == oldItem.user.avatarUrl
        } else true
    }

}
