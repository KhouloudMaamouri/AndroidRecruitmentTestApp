package com.apps.network.mapper

import com.apps.network.model.NetworkError
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import kotlin.coroutines.cancellation.CancellationException

/**
 * Unit tests for [toNetworkError].
 */
class ExceptionMapperTest {

    @Test
    fun http401_mapsToUnauthorized() {
        val exception = HttpException(Response.error<Any>(401, "".toResponseBody(null)))
        assertEquals(NetworkError.Unauthorized, exception.toNetworkError())
    }

    @Test
    fun http403_mapsToForbidden() {
        val exception = HttpException(Response.error<Any>(403, "".toResponseBody(null)))
        assertEquals(NetworkError.Forbidden, exception.toNetworkError())
    }

    @Test
    fun http404_mapsToNotFound() {
        val exception = HttpException(Response.error<Any>(404, "".toResponseBody(null)))
        assertEquals(NetworkError.NotFound, exception.toNetworkError())
    }

    @Test
    fun http416_mapsToRangeNotSatisfiable() {
        val exception = HttpException(Response.error<Any>(416, "".toResponseBody(null)))
        assertEquals(NetworkError.RangeNotSatisfiable, exception.toNetworkError())
    }

    @Test
    fun http429_mapsToTooManyRequests() {
        val exception = HttpException(Response.error<Any>(429, "".toResponseBody(null)))
        assertEquals(NetworkError.TooManyRequests, exception.toNetworkError())
    }

    @Test
    fun http500_mapsToServer() {
        val exception = HttpException(Response.error<Any>(500, "".toResponseBody(null)))
        assertEquals(NetworkError.Server, exception.toNetworkError())
    }

    @Test
    fun socketTimeoutException_mapsToTimeout() {
        val exception = SocketTimeoutException("timeout")
        assertEquals(NetworkError.Timeout, exception.toNetworkError())
    }

    @Test
    fun ioException_mapsToNoInternet() {
        val exception = IOException("network connection lost")
        assertEquals(NetworkError.NoInternet, exception.toNetworkError())
    }

    @Test(expected = CancellationException::class)
    fun cancellationException_isRethrown() {
        val exception = CancellationException("cancelled")
        exception.toNetworkError()
    }

    @Test
    fun unknownException_mapsToUnknown() {
        val exception = IllegalArgumentException("invalid argument")
        assertEquals(NetworkError.Unknown, exception.toNetworkError())
    }
}
