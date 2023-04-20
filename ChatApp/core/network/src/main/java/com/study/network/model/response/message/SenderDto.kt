package com.study.network.model.response.message


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SenderDto(
    @SerialName("email")
    val email: String?,
    @SerialName("full_name")
    val fullName: String?,
    @SerialName("id")
    val id: Int?
)
