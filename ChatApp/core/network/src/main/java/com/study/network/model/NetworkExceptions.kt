package com.study.network.model

class ConnectionLostException : RuntimeException()
class NetworkException(override val message: String?) : RuntimeException()
