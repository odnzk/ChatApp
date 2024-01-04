package com.study.chat.common.domain.model

data class Reaction(val messageId: Int, val userId: Int, val emoji: Emoji)
