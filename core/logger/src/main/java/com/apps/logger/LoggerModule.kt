package com.apps.logger

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing the singleton [AppLogger] implementation.
 */
@Module
@InstallIn(SingletonComponent::class)
object LoggerModule {

    /**
     * Provides a singleton instance of [AppLogger].
     *
     * @return The [JavaAppLogger] instance.
     */
    @Provides
    @Singleton
    fun provideAppLogger(): AppLogger {
        return JavaAppLogger()
    }
}