package com.apps.common.result

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for [AppResult].
 */
class AppResultTest {

    @Test
    fun success_holdsDataAndIsSuccessType() {
        val result: AppResult<String, String> = AppResult.Success("Hello")

        assertTrue(result is AppResult.Success)
        assertEquals("Hello", (result as AppResult.Success).data)
    }

    @Test
    fun error_holdsErrorAndIsErrorType() {
        val result: AppResult<String, String> = AppResult.Error("Failed")

        assertTrue(result is AppResult.Error)
        assertEquals("Failed", (result as AppResult.Error).error)
    }
}
