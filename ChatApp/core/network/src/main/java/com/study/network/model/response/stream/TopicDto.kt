package com.study.network.model.response.stream


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TopicDto(
    @SerialName("max_id")
    val maxId: Int?, // 26
    @SerialName("name")
    val name: String? // Denmark3
)
