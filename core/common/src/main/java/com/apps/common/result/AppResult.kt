package com.apps.common.result

/**
 * Generic sealed interface representing the result of an operation (Success or Error).
 *
 * @param T Type of successful data payload.
 * @param E Type of error payload.
 */
sealed interface AppResult<out T, out E> {

    /**
     * Represents a successful outcome containing data.
     *
     * @property data The success payload.
     */
    data class Success<T>(
        val data: T
    ) : AppResult<T, Nothing>

    /**
     * Represents a failed outcome containing an error.
     *
     * @property error The error payload.
     */
    data class Error<E>(
        val error: E
    ) : AppResult<Nothing, E>
}