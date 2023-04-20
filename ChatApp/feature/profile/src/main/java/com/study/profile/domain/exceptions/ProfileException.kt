package com.study.profile.domain.exceptions

internal sealed class ProfileException : RuntimeException()
internal class UserNotFoundException : ProfileException()
