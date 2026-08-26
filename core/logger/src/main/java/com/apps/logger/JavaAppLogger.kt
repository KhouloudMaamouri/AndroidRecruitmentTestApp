package com.apps.logger

import java.util.logging.Level
import java.util.logging.Logger

/**
 * Implementation of [AppLogger] delegating log messages to [java.util.logging.Logger].
 */
class JavaAppLogger : AppLogger {

    private val logger: Logger by lazy {
        Logger.getGlobal()
    }

    /**
     * Logs a FINE level debug message.
     *
     * @param message Debug log text.
     */
    override fun debug(message: String) {
        logger.fine(message)
    }

    /**
     * Logs an INFO level message.
     *
     * @param message Info log text.
     */
    override fun info(message: String) {
        logger.info(message)
    }

    /**
     * Logs a WARNING level message.
     *
     * @param message Warning log text.
     */
    override fun warning(message: String) {
        logger.warning(message)
    }

    /**
     * Logs a SEVERE level error message with optional throwable.
     *
     * @param message Error log text.
     * @param throwable Optional exception cause.
     */
    override fun error(
        message: String,
        throwable: Throwable?
    ) {
        logger.log(
            Level.SEVERE,
            message,
            throwable
        )
    }
}