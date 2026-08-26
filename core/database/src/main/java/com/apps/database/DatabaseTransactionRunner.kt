package com.apps.database

/**
 * Interface defining contract for executing blocks inside a database transaction scope.
 */
interface DatabaseTransactionRunner {

    /**
     * Executes the provided suspend block within a single database transaction.
     *
     * @param T Return type of the transaction block.
     * @param block The suspend lambda block to run within the transaction.
     * @return The result returned by [block].
     */
    suspend fun <T> withTransaction(
        block: suspend () -> T,
    ): T
}