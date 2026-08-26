package com.apps.ui.util

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

/**
 * Sealed interface representing UI text strings that can be either raw dynamic strings
 * or Android string resource IDs with optional formatting arguments.
 */
sealed interface UiText {
    /**
     * Represents a raw dynamic string value.
     *
     * @property value The literal string.
     */
    data class DynamicString(val value: String) : UiText

    /**
     * Represents a localized Android string resource with format arguments.
     *
     * @property resId The Android string resource ID.
     * @property args Format arguments to apply to the string resource.
     */
    class StringResource(val resId: Int, vararg val args: Any) : UiText

    /**
     * Resolves the [UiText] instance into a [String] using an Android [Context].
     *
     * @param context Context used to resolve string resources.
     * @return The resolved string.
     */
    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> context.getString(resId, *args)
        }
    }

    /**
     * Resolves the [UiText] instance into a [String] within a Composable scope.
     *
     * @return The resolved string.
     */
    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> stringResource(resId, *args)
        }
    }
}