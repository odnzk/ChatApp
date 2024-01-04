package com.study.profile.domain.model

internal sealed class ProfileException : RuntimeException()
internal class UserNotFoundException : ProfileException()
