package com.study.chat.chat.presentation.model

import java.util.Calendar

internal sealed interface ChatListItem

internal class DateSeparator(val calendar: Calendar) : ChatListItem

internal class TopicSeparator(val topicTitle: String, val calendar: Calendar) : ChatListItem
