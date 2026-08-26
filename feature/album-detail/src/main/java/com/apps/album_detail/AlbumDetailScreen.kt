package com.apps.album_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material3.SuggestionChip
import coil3.compose.AsyncImage
import com.adevinta.spark.SparkTheme
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextOverflow

/**
 * Composable screen displaying details for a specific album.
 *
 * Shows full-resolution album image, title, album/item IDs, and favorite status toggle.
 *
 * @param state The current UI state containing album data, loading status, or error.
 * @param onIntent Callback for handling user intents like retrying or toggling favorite status.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AlbumDetailScreen(
    state: AlbumDetailUiState,
    onIntent: (AlbumDetailIntent) -> Unit,
) {
    val context = LocalContext.current

    when {
        state.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        state.error != null && state.album == null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = state.error.asString(context))
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { onIntent(AlbumDetailIntent.Retry) }) {
                        Text(text = stringResource(R.string.retry))
                    }
                }
            }
        }

        state.album != null -> {
            val album = state.album
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                AsyncImage(
                    model = album.url,
                    contentDescription = album.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = album.title,
                            style = SparkTheme.typography.headline1,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    IconButton(
                        onClick = { onIntent(AlbumDetailIntent.ToggleFavorite) }
                    ) {
                        Icon(
                            imageVector = if (album.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = stringResource(R.string.content_description_toggle_favorite),
                            tint = if (album.isFavorite) Color.Red else Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.Top,
                ) {
                    SuggestionChip(
                        onClick = {},
                        label = {
                            Text(
                                text = stringResource(R.string.album_id_format, album.albumId),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                    SuggestionChip(
                        onClick = {},
                        label = {
                            Text(
                                text = stringResource(R.string.item_id_format, album.id),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                }
            }
        }
    }
}