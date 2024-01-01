package com.study.network.model.response.user


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DetailedUserDto(
    @SerialName("avatar_url") val avatarUrl: String?,
    @SerialName("email") val email: String,
    @SerialName("full_name") val fullName: String,
    @SerialName("user_id") val userId: Int,
    @SerialName("is_admin") val isAdmin: Boolean,
    @SerialName("is_active") val isActive: Boolean,
    @SerialName("is_bot") val isBot: Boolean,
    @SerialName("is_owner") val isOwner: Boolean,
    @SerialName("is_guest") val isGuest: Boolean,
    @SerialName("is_billing_admin") val isBillingAdmin: Boolean
)
