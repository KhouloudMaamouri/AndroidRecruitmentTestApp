package fr.leboncoin.androidrecruitmenttestapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application entry point for dependency injection setup via Hilt.
 */
@HiltAndroidApp
class MainApplication : Application()