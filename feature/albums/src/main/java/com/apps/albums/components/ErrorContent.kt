package com.apps.albums.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.ui.res.stringResource
import com.apps.albums.R
import com.apps.ui.util.UiText

/**
 * Full-screen error state composable displaying an error message and a retry button.
 *
 * @param message The [UiText] error message to display.
 * @param onRetry Callback invoked when the user taps the retry button.
 */
@Composable
fun ErrorContent(
    message: UiText,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(
            text = message.asString()
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Button(
            onClick = onRetry
        ) {
            Text(stringResource(R.string.retry))
        }
    }
}