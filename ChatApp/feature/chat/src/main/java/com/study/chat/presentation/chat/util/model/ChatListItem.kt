package com.study.chat.presentation.chat.util.model

import java.util.Calendar

internal sealed interface ChatListItem
internal class DateSeparator(val calendar: Calendar) : ChatListItem
internal class TopicSeparator(val topicTitle: String, val calendar: Calendar) : ChatListItem
