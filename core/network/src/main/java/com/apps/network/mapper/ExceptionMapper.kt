package com.apps.network.mapper

import com.apps.network.model.NetworkError
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import kotlin.coroutines.cancellation.CancellationException

/**
 * Maps a [Throwable] exception to a domain-level [NetworkError].
 * Re-throws [CancellationException] to preserve coroutine cancellation behavior.
 *
 * @return The corresponding [NetworkError] enum or object representation.
 */
fun Throwable.toNetworkError(): NetworkError {
    if (this is CancellationException) throw this

    return when (this) {
        is HttpException -> when (code()) {
            401 -> NetworkError.Unauthorized
            403 -> NetworkError.Forbidden
            404 -> NetworkError.NotFound
            416 -> NetworkError.RangeNotSatisfiable
            429 -> NetworkError.TooManyRequests
            in 500..599 -> NetworkError.Server
            else -> NetworkError.Unknown
        }
        is SocketTimeoutException -> NetworkError.Timeout
        is IOException -> NetworkError.NoInternet
        else -> NetworkError.Unknown
    }
}