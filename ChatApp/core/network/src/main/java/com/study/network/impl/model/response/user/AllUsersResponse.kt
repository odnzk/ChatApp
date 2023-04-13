package com.study.network.impl.model.response.user


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AllUsersResponse(
    @SerialName("members")
    val members: List<DetailedUserDto?>?
)
