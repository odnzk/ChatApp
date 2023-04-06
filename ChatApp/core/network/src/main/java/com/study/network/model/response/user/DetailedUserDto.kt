package com.study.network.model.response.user


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DetailedUserDto(
    @SerialName("avatar_url") val avatarUrl: String?, // https://secure.gravatar.com/avatar/af4f06322c177ef4e1e9b2c424986b54?d=identicon&version=1
    @SerialName("date_joined") val dateJoined: String?, // 2019-10-20T07:50:53.728864+00:00
    @SerialName("email") val email: String?, // iago@zulip.com
    @SerialName("full_name") val fullName: String?, // Iago
    @SerialName("is_active") val isActive: Boolean?, // true
    @SerialName("is_admin") val isAdmin: Boolean?, // true
    @SerialName("is_billing_admin") val isBillingAdmin: Boolean?, // false
    @SerialName("is_bot") val isBot: Boolean?, // false
    @SerialName("is_guest") val isGuest: Boolean?, // false
    @SerialName("is_owner") val isOwner: Boolean?, // false
    @SerialName("role") val role: Int?, // 200
    @SerialName("timezone") val timezone: String?, @SerialName("user_id") val userId: Int? // 5
)
