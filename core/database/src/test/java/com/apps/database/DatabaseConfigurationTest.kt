package com.apps.database

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for [DatabaseConfiguration].
 */
class DatabaseConfigurationTest {

    @Test
    fun defaultConfiguration_hasExpectedDefaults() {
        val config = DatabaseConfiguration()
        assertEquals("app.db", config.name)

    }

    @Test
    fun customConfiguration_overridesDefaults() {
        val config = DatabaseConfiguration(name = "custom.db")
        assertEquals("custom.db", config.name)
    }
}
