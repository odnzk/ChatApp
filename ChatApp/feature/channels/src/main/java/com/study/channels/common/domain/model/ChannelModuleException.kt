package com.study.channels.common.domain.model

internal sealed class ChannelModuleException : RuntimeException()
internal class ChannelNotFoundException : ChannelModuleException()
internal class ChannelDoesNotHaveTopicsException : ChannelModuleException()
internal class ChannelAlreadyExistsException : ChannelModuleException()
internal class InvalidChannelTitleException(val maxLength: Int) : ChannelModuleException()
internal class ServerSynchronizationException : ChannelModuleException()
