package com.apps.ui.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for [UiText].
 */
class UiTextTest {

    @Test
    fun dynamicString_storesAndRetrievesValue() {
        val text = UiText.DynamicString("Sample Dynamic String")
        assertEquals("Sample Dynamic String", text.value)
        assertTrue(text is UiText.DynamicString)
    }

    @Test
    fun stringResource_storesResIdAndArgs() {
        val text = UiText.StringResource(123, "arg1", 456)
        assertEquals(123, text.resId)
        assertEquals(2, text.args.size)
        assertEquals("arg1", text.args[0])
        assertEquals(456, text.args[1])
    }
}
