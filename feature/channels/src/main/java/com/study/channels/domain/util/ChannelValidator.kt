package com.study.channels.domain.util

import com.study.channels.domain.model.Channel
import com.study.channels.domain.model.InvalidChannelTitleException
import com.study.common.validation.Validator
import javax.inject.Inject

internal class ChannelValidator @Inject constructor() : Validator<Channel> {
    override fun validate(item: Channel) {
        if (item.title.isBlank() || item.title.length > MAX_CHANNEL_TITLE_LENGTH) {
            throw InvalidChannelTitleException(MAX_CHANNEL_TITLE_LENGTH)
        }
    }

    companion object {
        private const val MAX_CHANNEL_TITLE_LENGTH = 200
    }
}
