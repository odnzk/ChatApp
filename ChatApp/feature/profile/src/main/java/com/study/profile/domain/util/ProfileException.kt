package com.study.profile.domain.util

internal sealed class ProfileException : Exception()
internal class UserNotAuthorizedException : ProfileException()
internal class UserNotFoundException : ProfileException()
