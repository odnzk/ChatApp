package com.study.network.impl.model.response.stream


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubscribedStreamsResponse(
    @SerialName("subscriptions")
    val subscriptions: List<SubscriptionDto?>?
)
