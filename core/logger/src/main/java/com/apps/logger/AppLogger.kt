package com.apps.logger

/**
 * Interface defining unified logging operations across application modules.
 */
interface AppLogger {

    /**
     * Logs a debug level message.
     *
     * @param message Debug log content.
     */
    fun debug(message: String)

    /**
     * Logs an informational message.
     *
     * @param message Info log content.
     */
    fun info(message: String)

    /**
     * Logs a warning level message.
     *
     * @param message Warning log content.
     */
    fun warning(message: String)

    /**
     * Logs an error level message with optional throwable details.
     *
     * @param message Error log content.
     * @param throwable Optional [Throwable] exception cause.
     */
    fun error(
        message: String,
        throwable: Throwable? = null
    )
}