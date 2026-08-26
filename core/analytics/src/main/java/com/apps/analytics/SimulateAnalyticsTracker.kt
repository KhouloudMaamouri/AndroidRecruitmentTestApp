package com.apps.analytics

import androidx.core.os.bundleOf

/**
 * Simulated implementation of [AnalyticsTracker] printing tracking logs to system console.
 */
class SimulateAnalyticsTracker(
) : AnalyticsTracker {

    /**
     * Tracks screen view events by printing a formatted bundle string.
     *
     * @param screen The target [AnalyticsScreen].
     */
    override fun trackScreen(screen: AnalyticsScreen) {
        println("Analytics: screen_view - ${ bundleOf(
            "screen_name" to screen
        )}")
    }

    /**
     * Tracks analytics events by printing formatted event details.
     *
     * @param event The target [AnalyticsEvent].
     */
    override fun trackEvent(event: AnalyticsEvent) {
        when (event) {
            is AnalyticsEvent.AlbumSelected -> {
                println("Analytics: User selected item - ${bundleOf(
                    "album_id" to event.albumId
                )}")
            }

            AnalyticsEvent.AlbumsRefresh -> {
                println("albums_refresh")
            }

            is AnalyticsEvent.AlbumsLoadError -> {
                println("albums_load_error - ${bundleOf(
                    "error" to event.error
                )}")
            }
        }
    }
}