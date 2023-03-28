package com.study.chat.domain.model

data class Reaction(val messageId: Int, val userId: Int, val emoji: Emoji)
