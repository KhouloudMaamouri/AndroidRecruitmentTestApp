package com.apps.analytics

/**
 * Contract interface for tracking application screen views and user event metrics.
 */
interface AnalyticsTracker {

    /**
     * Tracks a screen view navigation event.
     *
     * @param screen The [AnalyticsScreen] being viewed.
     */
    fun trackScreen(
        screen: AnalyticsScreen
    )

    /**
     * Tracks a user interaction or state event.
     *
     * @param event The [AnalyticsEvent] being recorded.
     */
    fun trackEvent(
        event: AnalyticsEvent
    )
}