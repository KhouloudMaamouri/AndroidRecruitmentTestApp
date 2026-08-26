package com.apps.network.model

/**
 * Sealed interface representing network error cases.
 */
sealed interface NetworkError {
    /** Resource not found (HTTP 404). */
    data object NotFound : NetworkError
    /** Range not satisfiable error (HTTP 416). */
    data object RangeNotSatisfiable : NetworkError
    /** Too many requests rate limit error (HTTP 429). */
    data object TooManyRequests : NetworkError
    /** Server internal error (HTTP 5xx). */
    data object Server : NetworkError
    /** Unauthorized error (HTTP 401). */
    data object Unauthorized : NetworkError
    /** Forbidden error (HTTP 403). */
    data object Forbidden : NetworkError
    /** Connectivity failure / no internet connection. */
    data object NoInternet : NetworkError
    /** Network socket read/connect timeout error. */
    data object Timeout : NetworkError
    /** Unknown or unhandled network error. */
    data object Unknown : NetworkError
}