package fr.leboncoin.androidrecruitmenttestapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.apps.album_detail.AlbumDetailRoute
import com.apps.albums.AlbumsRoute

/**
 * Navigation host configuring top-level application navigation routes.
 *
 * Defines navigation routes between [AppRoute.Albums] and [AppRoute.AlbumDetails].
 *
 * @param navController The [NavHostController] managing app navigation state.
 */
@Composable
fun AppNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = AppRoute.Albums
    ) {

        composable<AppRoute.Albums> {

            AlbumsRoute(
                onAlbumSelected = { albumId ->
                    navController.navigate(
                        AppRoute.AlbumDetails(albumId)
                    )
                }
            )
        }

        composable<AppRoute.AlbumDetails> { backStackEntry ->

            val route = backStackEntry.toRoute<AppRoute.AlbumDetails>()

            AlbumDetailRoute(
                albumId = route.albumId
            )
        }
    }
}