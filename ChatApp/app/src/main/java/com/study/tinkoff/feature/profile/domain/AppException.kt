package com.study.tinkoff.feature.profile.domain

sealed class AppException : Exception()
class UserNotAuthorizedException : AppException()
class UserNotFoundException : AppException()
