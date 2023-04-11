package com.study.auth

class UserNotAuthorizedException(override val message: String? = null) : RuntimeException()
