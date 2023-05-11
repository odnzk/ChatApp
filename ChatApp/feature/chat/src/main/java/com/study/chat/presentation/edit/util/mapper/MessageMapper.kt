package com.study.chat.presentation.edit.util.mapper

import com.study.chat.domain.model.IncomeMessage
import com.study.chat.presentation.edit.util.EditableMessage

internal fun IncomeMessage.toEditableMessage(): EditableMessage = EditableMessage(
    messageId = id, content = content, topic = topic, channelId = channelId
)
