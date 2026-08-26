package com.apps.album.error

/**
 * Sealed interface representing domain-level error cases for album operations.
 */
sealed interface AlbumError {
    /** Error triggered when network connectivity is unavailable. */
    data object NetworkUnavailable : AlbumError
    /** Error triggered when a remote server returns an error code or invalid payload. */
    data object ServerError : AlbumError
    /** Error triggered when a local database read/write operation fails. */
    data object DatabaseError : AlbumError
    /** Fallback error for unexpected or unhandled exception scenarios. */
    data object Unknown : AlbumError
}