package com.study.network.model.response.stream


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubscribedStreamsResponse(
    @SerialName("subscriptions")
    val subscriptions: List<SubscriptionDto?>?
)
