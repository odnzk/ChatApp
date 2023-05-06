package com.study.network.model.response.stream


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddStreamResponse(
    @SerialName("already_subscribed")
    val alreadySubscribed: Map<String?, List<String?>?>?
)
