package com.study.auth.api

import java.io.IOException

class UserNotAuthorizedException(override val message: String? = null) : IOException()
// IOException() for throeing this exception in OkHttp Interceptors