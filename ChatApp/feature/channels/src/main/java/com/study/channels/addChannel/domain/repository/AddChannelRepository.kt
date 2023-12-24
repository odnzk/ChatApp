package com.study.channels.addChannel.domain.repository

import com.study.channels.common.domain.model.Channel

internal interface AddChannelRepository {
    suspend fun addChannel(channel: Channel)
}
