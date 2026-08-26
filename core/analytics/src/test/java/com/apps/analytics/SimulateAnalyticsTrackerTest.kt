package com.apps.analytics

import org.junit.Test

/**
 * Unit tests for [SimulateAnalyticsTracker].
 */
class SimulateAnalyticsTrackerTest {

    private val tracker: AnalyticsTracker = SimulateAnalyticsTracker()

    @Test
    fun trackScreen_doesNotThrowException() {
        tracker.trackScreen(AnalyticsScreen.ALBUMS)
        tracker.trackScreen(AnalyticsScreen.ALBUM_DETAILS)
    }

    @Test
    fun trackEvent_doesNotThrowException() {
        tracker.trackEvent(AnalyticsEvent.AlbumSelected(10))
        tracker.trackEvent(AnalyticsEvent.AlbumsRefresh)
        tracker.trackEvent(AnalyticsEvent.AlbumsLoadError("Network Error"))
    }
}
