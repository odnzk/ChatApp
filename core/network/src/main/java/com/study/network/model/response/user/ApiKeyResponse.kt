package com.study.network.model.response.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ApiKeyResponse(
    @SerialName("api_key")
    val apiKey: String,
    @SerialName("user_id")
    val userId: Int
)