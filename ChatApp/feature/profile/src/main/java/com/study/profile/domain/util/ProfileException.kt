package com.study.profile.domain.util

sealed class ProfileException : Exception()
class UserNotAuthorizedException : ProfileException()
class UserNotFoundException : ProfileException()
