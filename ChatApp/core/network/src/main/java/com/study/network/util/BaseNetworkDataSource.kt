package com.study.network.util

import com.study.network.model.ConnectionLostException
import com.study.network.model.NetworkException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException

/**
 * A base class for data sources to encapsulate work with the network.
 *
 */
open class BaseNetworkDataSource {
    /**
     * Makes a safe network request using the provided suspend function [request].
     * If an [HttpException] or an [IOException]  occurs, it will throw an appropriate exception.
     *
     * @param request The suspend function to make the network request.
     *
     * @return The result of the network request.
     *
     * @throws NetworkException if an HTTP exception occurs.
     * @throws ConnectionLostException if an IO exception occurs.
     */
    suspend inline fun <T : Any> safeRequest(crossinline request: suspend () -> T): T =
        withContext(Dispatchers.IO) {
            try {
                request()
            } catch (ex: UnknownHostException) {
                throw ConnectionLostException()
            } catch (ex: HttpException) {
                throw NetworkException(ex.message)
            } catch (ex: IOException) {
                throw ConnectionLostException()
            }
        }
}
