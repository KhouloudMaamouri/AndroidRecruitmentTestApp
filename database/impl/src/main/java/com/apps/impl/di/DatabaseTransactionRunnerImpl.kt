package com.apps.impl.di

import androidx.room.withTransaction
import com.apps.database.DatabaseTransactionRunner
import com.apps.impl.AppDatabase

/**
 * Concrete implementation of [DatabaseTransactionRunner] for executing operations in a Room database transaction.
 *
 * @property database The target [AppDatabase].
 */
class DatabaseTransactionRunnerImpl(
    private val database: AppDatabase
) : DatabaseTransactionRunner {

    /**
     * Runs a suspend block inside a Room database transaction.
     *
     * @param T Return type of the transaction block.
     * @param block The suspend block to execute inside transaction context.
     * @return The result produced by [block].
     */
    override suspend fun <T> withTransaction(
        block: suspend () -> T
    ): T {
        return database.withTransaction {
            block()
        }
    }
}