package fr.leboncoin.androidrecruitmenttestapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.adevinta.spark.SparkTheme
import fr.leboncoin.androidrecruitmenttestapp.navigation.AppNavHost
import fr.leboncoin.androidrecruitmenttestapp.ui.AppScaffold

/**
 * Root Composable for the application.
 *
 * Sets up the [SparkTheme], initializes the [rememberNavController],
 * and renders the top-level [AppScaffold] containing the [AppNavHost].
 */
@Composable
fun App() {
    SparkTheme {
        val navController = rememberNavController()

        AppScaffold(
            navController = navController
        ) { paddingValues ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                AppNavHost(
                    navController = navController
                )
            }
        }
    }
}