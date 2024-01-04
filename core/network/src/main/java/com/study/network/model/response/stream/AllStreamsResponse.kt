package com.study.network.model.response.stream


import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class AllStreamsResponse(
    @SerialName("streams")
    @JsonNames("subscriptions")
    val streams: List<StreamDto?>?
)
