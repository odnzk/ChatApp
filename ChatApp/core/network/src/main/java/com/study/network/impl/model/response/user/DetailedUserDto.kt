package com.study.network.impl.model.response.user


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DetailedUserDto(
    @SerialName("avatar_url") val avatarUrl: String?,
    @SerialName("date_joined") val dateJoined: String?,
    @SerialName("email") val email: String?,
    @SerialName("full_name") val fullName: String?,
    @SerialName("is_active") val isActive: Boolean?,
    @SerialName("is_admin") val isAdmin: Boolean?,
    @SerialName("is_billing_admin") val isBillingAdmin: Boolean?,
    @SerialName("is_bot") val isBot: Boolean?,
    @SerialName("is_guest") val isGuest: Boolean?,
    @SerialName("is_owner") val isOwner: Boolean?,
    @SerialName("role") val role: Int?,
    @SerialName("timezone") val timezone: String?,
    @SerialName("user_id") val userId: Int?
)
