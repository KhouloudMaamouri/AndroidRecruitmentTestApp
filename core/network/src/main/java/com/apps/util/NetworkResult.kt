package com.apps.util

import com.apps.network.mapper.toNetworkError
import com.apps.network.model.NetworkError
import kotlin.coroutines.cancellation.CancellationException

/**
 * Sealed interface representing network execution result (Success or Error).
 *
 * @param T Type of response data.
 */
sealed interface NetworkResult<out T> {
    /**
     * Successful network call result.
     *
     * @property data Successful data payload.
     */
    data class Success<T>(val data: T) : NetworkResult<T>

    /**
     * Failed network call result.
     *
     * @property error Network error payload.
     */
    data class Error(val error: NetworkError) : NetworkResult<Nothing>
}

/**
 * Safely executes a network call block catching exceptions and mapping them to [NetworkResult].
 * Re-throws coroutine [CancellationException].
 *
 * @param T Expected return type.
 * @param block The suspend network call block to execute.
 * @return A [NetworkResult] wrapping data or mapped error.
 */
suspend inline fun <T> safeApiCall(block: () -> T): NetworkResult<T> {
    return try {
        NetworkResult.Success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        NetworkResult.Error(e.toNetworkError())
    }
}