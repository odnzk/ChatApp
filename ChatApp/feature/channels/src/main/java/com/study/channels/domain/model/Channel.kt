package com.study.channels.domain.model

data class Channel(
    val id: Int,
    val title: String,
    val description: String,
    val isPrivate: Boolean,
    val streamPostPolicy: StreamPostPolicy
)

enum class StreamPostPolicy {
    ANY_USERS, ONLY_ADMISTRATORS, ONLY_FULL_MEMBERS, ONLY_MODERATORS
}
