package com.study.chat.shared.domain.util

import com.study.chat.shared.domain.model.InvalidTopicTitleException
import com.study.chat.shared.domain.model.OutcomeMessage
import com.study.common.validation.Validator
import javax.inject.Inject

internal class MessageValidator @Inject constructor() : Validator<OutcomeMessage> {
    override fun validate(item: OutcomeMessage) = when {
        item.topicTitle.isBlank() || item.topicTitle.length > MAX_TOPIC_LENGTH ->
            throw InvalidTopicTitleException(MAX_TOPIC_LENGTH)
        else -> Unit
    }


    companion object {
        private const val MAX_TOPIC_LENGTH = 50
    }
}
