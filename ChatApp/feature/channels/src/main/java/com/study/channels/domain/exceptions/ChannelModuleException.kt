package com.study.channels.domain.exceptions

internal sealed class ChannelModuleException : RuntimeException()

internal class ChannelNotFoundException : ChannelModuleException()
internal class ChannelDoesNotHaveTopicsException : ChannelModuleException()
internal class ChannelAlreadyExistsException : ChannelModuleException()
internal class InvalidChannelTitleException(val maxLength: Int) : ChannelModuleException()
