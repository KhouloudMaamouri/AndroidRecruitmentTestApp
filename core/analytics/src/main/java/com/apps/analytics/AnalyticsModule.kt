package com.apps.analytics

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module providing analytics tracker instances.
 */
@Module
@InstallIn(SingletonComponent::class)
object AnalyticsModule {

    /**
     * Provides a singleton instance of [AnalyticsTracker].
     *
     * @return The configured [AnalyticsTracker] instance.
     */
    @Provides
    @Singleton
    fun provideAnalyticsTracker(): AnalyticsTracker {
        return SimulateAnalyticsTracker()
    }
}
