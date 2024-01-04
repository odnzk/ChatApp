package com.study.chat.common.domain.model

internal sealed class ChatModuleException : RuntimeException()
internal class SynchronizationException : ChatModuleException()
internal class ContentHasNotLoadedException : ChatModuleException()
internal class InvalidTopicTitleException(val maxLength: Int) : ChatModuleException()
internal class InvalidMessageContentException(val maxLength: Int) : ChatModuleException()
internal class MessageDoesNotExistException : ChatModuleException()
