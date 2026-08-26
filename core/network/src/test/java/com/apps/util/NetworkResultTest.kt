package com.apps.util

import com.apps.network.model.NetworkError
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

/**
 * Unit tests for [safeApiCall] and [NetworkResult].
 */
class NetworkResultTest {

    @Test
    fun safeApiCall_successfulBlock_returnsSuccess() = runTest {
        val result = safeApiCall { "success_data" }

        assertTrue(result is NetworkResult.Success)
        assertEquals("success_data", (result as NetworkResult.Success).data)
    }

    @Test
    fun safeApiCall_ioException_returnsErrorNoInternet() = runTest {
        val result = safeApiCall {
            throw IOException("No connection")
        }

        assertTrue(result is NetworkResult.Error)
        assertEquals(NetworkError.NoInternet, (result as NetworkResult.Error).error)
    }

    @Test(expected = CancellationException::class)
    fun safeApiCall_cancellationException_rethrows() = runTest {
        safeApiCall {
            throw CancellationException("Cancelled")
        }
    }
}
