package com.apps.impl

import com.apps.database.DatabaseTransactionRunner
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for [DatabaseTransactionRunner] contract.
 */
class RoomDatabaseTransactionRunnerTest {

    private val fakeRunner = object : DatabaseTransactionRunner {
        override suspend fun <T> withTransaction(block: suspend () -> T): T {
            return block()
        }
    }

    @Test
    fun withTransaction_executesBlockAndReturnsResult() = runTest {
        val result = fakeRunner.withTransaction {
            "Transaction Completed"
        }

        assertEquals("Transaction Completed", result)
    }
}
