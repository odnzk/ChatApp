package com.study.chat.domain.util

import com.study.chat.domain.exceptions.InvalidMessageContentException
import com.study.chat.domain.exceptions.InvalidTopicTitleException
import com.study.chat.domain.model.OutcomeMessage
import com.study.common.Validator
import javax.inject.Inject

internal class MessageValidator @Inject constructor() : Validator<OutcomeMessage> {
    override fun validate(item: OutcomeMessage) {
        when {
            item.topicTitle.isBlank() || item.topicTitle.length > MAX_TOPIC_LENGTH ->
                throw InvalidTopicTitleException(MAX_TOPIC_LENGTH)
            item.content.isBlank() || item.content.length > MAX_MESSAGE_LENGTH ->
                throw InvalidMessageContentException(MAX_MESSAGE_LENGTH)
        }
    }

    companion object {
        private const val MAX_TOPIC_LENGTH = 50
        private const val MAX_MESSAGE_LENGTH = 10000
    }
}
