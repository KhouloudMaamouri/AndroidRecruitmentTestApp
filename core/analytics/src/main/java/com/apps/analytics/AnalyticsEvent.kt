package com.apps.analytics

/**
 * Sealed interface representing trackable user interaction and analytics events.
 */
sealed interface AnalyticsEvent {

    /**
     * Analytics event recorded when an album item is selected.
     *
     * @property albumId Unique identifier of the selected album.
     */
    data class AlbumSelected(
        val albumId: Int
    ) : AnalyticsEvent

    /** Analytics event recorded when album data is refreshed. */
    data object AlbumsRefresh : AnalyticsEvent

    /**
     * Analytics event recorded when an error occurs during album loading.
     *
     * @property error Error message details.
     */
    data class AlbumsLoadError(
        val error: String
    ) : AnalyticsEvent
}