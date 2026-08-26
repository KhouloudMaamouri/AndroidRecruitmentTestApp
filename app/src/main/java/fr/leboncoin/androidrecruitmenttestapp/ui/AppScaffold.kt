package fr.leboncoin.androidrecruitmenttestapp.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hasRoute
import fr.leboncoin.androidrecruitmenttestapp.navigation.AppRoute

/**
 * Top-level Scaffold component for the application.
 *
 * Automatically displays the [AppTopBar] with the current screen's title and
 * back navigation button based on the current back stack entry.
 *
 * @param navController The [NavHostController] managing navigation state.
 * @param content The composable content slot receiving scaffold [PaddingValues].
 */
@Composable
fun AppScaffold(
    navController: NavHostController,
    content: @Composable (PaddingValues) -> Unit
) {
    val backStackEntry by navController.currentBackStackEntryAsState()

    val canNavigateBack = navController.previousBackStackEntry != null

    val title = when {
        backStackEntry?.destination
            ?.hasRoute<AppRoute.Albums>() == true -> {
            stringResource(fr.leboncoin.androidrecruitmenttestapp.R.string.screen_name_album)

        }

        backStackEntry?.destination
            ?.hasRoute<AppRoute.AlbumDetails>() == true -> {
            stringResource(fr.leboncoin.androidrecruitmenttestapp.R.string.screen_name_album_details)
        }

        else -> {
            ""
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = title,
                canNavigateBack = canNavigateBack,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        },
        content = content
    )
}