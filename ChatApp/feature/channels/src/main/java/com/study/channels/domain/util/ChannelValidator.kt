package com.study.channels.domain.util

import com.study.channels.domain.exceptions.InvalidChannelTitleException
import com.study.channels.domain.model.Channel
import com.study.common.Validator
import javax.inject.Inject

internal class ChannelValidator @Inject constructor() : Validator<Channel> {
    override fun validate(item: Channel) {
        if (item.title.isBlank() || item.title.length > MAX_CHANNEL_LENGTH) {
            throw InvalidChannelTitleException(MAX_CHANNEL_LENGTH)
        }
    }

    companion object {
        private const val MAX_CHANNEL_LENGTH = 200
    }
}
