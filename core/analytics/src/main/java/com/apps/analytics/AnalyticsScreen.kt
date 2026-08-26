package com.apps.analytics

/**
 * Enum defining screen names tracked by the analytics engine.
 *
 * @property value String identifier for the screen view event.
 */
enum class AnalyticsScreen(
    val value: String
) {
    /** Albums list screen. */
    ALBUMS("albums"),
    /** Album details screen. */
    ALBUM_DETAILS("album_details")
}