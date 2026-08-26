package com.apps.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

/**
 * Hilt module for providing global application coroutine scope.
 */
@Module
@InstallIn(SingletonComponent::class)
object CoroutinesModule {

    /**
     * Provides a singleton instance of [CoroutineScope] configured with [SupervisorJob] and [Dispatchers.IO].
     *
     * @return The configured [CoroutineScope].
     */
    @Provides
    @Singleton
    fun provideCoroutineScope(): CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.IO)
}
