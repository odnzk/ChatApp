package com.study.network.model.response.message


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UploadedFileResponse(
    @SerialName("uri")
    val uri: String?
)
