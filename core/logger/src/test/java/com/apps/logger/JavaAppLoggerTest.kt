package com.apps.logger

import org.junit.Test

/**
 * Unit tests for [JavaAppLogger].
 */
class JavaAppLoggerTest {

    private val logger: AppLogger = JavaAppLogger()

    @Test
    fun debug_doesNotThrowException() {
        logger.debug("Test debug message")
    }

    @Test
    fun info_doesNotThrowException() {
        logger.info("Test info message")
    }

    @Test
    fun warning_doesNotThrowException() {
        logger.warning("Test warning message")
    }

    @Test
    fun error_doesNotThrowException() {
        logger.error("Test error message", RuntimeException("Sample exception"))
    }
}
