package com.apps.impl

import androidx.room.withTransaction
import com.apps.database.DatabaseTransactionRunner

/**
 * Implementation of [DatabaseTransactionRunner] wrapping Room's `withTransaction` extension.
 *
 * @property database The target [AppDatabase] for database transactions.
 */
class RoomDatabaseTransactionRunner(
    private val database: AppDatabase,
) : DatabaseTransactionRunner {

    /**
     * Executes the given suspend block within a Room database transaction.
     *
     * @param T Return type of the transaction block.
     * @param block The suspend lambda block to execute.
     * @return The result produced by [block].
     */
    override suspend fun <T> withTransaction(
        block: suspend () -> T,
    ): T {
        return database.withTransaction {
            block()
        }
    }
}