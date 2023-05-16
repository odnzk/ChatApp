package com.study.chat.edit.presentation.util

import com.study.chat.edit.presentation.model.EditableMessage
import com.study.chat.shared.domain.model.IncomeMessage

internal fun IncomeMessage.toEditableMessage(): EditableMessage = EditableMessage(
    messageId = id, content = content, topic = topic, channelId = channelId
)
