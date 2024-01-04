package com.study.chat.common.data.mapper

import com.study.network.model.response.stream.StreamTopicsResponse
internal fun StreamTopicsResponse.toChannelTopics(): List<String> =
    topics?.filterNotNull()?.map { it.name } ?: emptyList()