package fr.leboncoin.androidrecruitmenttestapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main activity for the App.
 * Annotated with [@AndroidEntryPoint] for Hilt dependency injection.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /**
     * Initializes the activity, enables edge-to-edge layout, and sets the root Compose UI content.
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            App()
        }
    }
}