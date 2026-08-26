package com.apps.albums.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SuggestionChip
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.card.Card
import com.adevinta.spark.components.spacer.Spacer
import com.adevinta.spark.components.text.Text
import com.apps.album.model.Album
import com.apps.albums.R

/**
 * Composable item displaying an album entry card with thumbnail, title, badges, and optional favorite toggle.
 *
 * @param album The [Album] model containing album details.
 * @param onItemSelected Callback triggered when the item card is clicked.
 * @param onFavoriteToggle Optional callback triggered when the favorite button is clicked.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AlbumItem(
    album: Album,
    onItemSelected: (Album) -> Unit,
    onFavoriteToggle: (() -> Unit)? = null,
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 120.dp)
            .padding(horizontal = 16.dp),
        onClick = {
            onItemSelected(album)
        },
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AlbumThumbnail(
                url = album.thumbnailUrl,
                contentDescription = album.title,
                modifier = Modifier
                    .height(120.dp)
                    .aspectRatio(1f)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(14.dp),
            ) {
                Text(
                    text = album.title,
                    style = SparkTheme.typography.caption,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(
                    modifier = Modifier.height(8.dp),
                )

                Column (
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start,
                ) {
                    SuggestionChip(
                        onClick = {},
                        label = {
                            androidx.compose.material3.Text(
                                text = stringResource(R.string.album_id_format, album.albumId),
                                style = SparkTheme.typography.caption,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    SuggestionChip(
                        onClick = {},
                        label = {
                            androidx.compose.material3.Text(
                                text = stringResource(R.string.item_id_format, album.id),
                                style = SparkTheme.typography.caption,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    )
                }
            }

            if (onFavoriteToggle != null) {
                IconButton(
                    onClick = onFavoriteToggle,
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Icon(
                        imageVector = if (album.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = stringResource(R.string.content_description_favorite),
                        tint = if (album.isFavorite) Color.Red else Color.Gray
                    )
                }
            }
        }
    }
}