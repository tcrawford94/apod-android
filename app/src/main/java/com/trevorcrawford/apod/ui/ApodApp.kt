package com.trevorcrawford.apod.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.trevorcrawford.apod.ui.astronomypicture.AstronomyPicturesScreen
import com.trevorcrawford.apod.ui.theme.ApodTheme
import com.trevorcrawford.apod.ui.util.SnackbarManager

@Composable
fun ApodApp(snackbarManager: SnackbarManager) {
    ApodTheme {
        val appState = rememberApodAppState(snackbarManager = snackbarManager)
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = {
                SnackbarHost(
                    hostState = it,
                    modifier = Modifier.systemBarsPadding(),
                    snackbar = { snackbarData -> Snackbar(snackbarData) }
                )
            },
            scaffoldState = appState.scaffoldState
        ) { innerPaddingModifier ->
            NavHost(
                navController = appState.navController,
                startDestination = MainDestinations.RANDOM_APOD_ROUTE,
                modifier = Modifier.padding(innerPaddingModifier)
            ) {
                composable(MainDestinations.RANDOM_APOD_ROUTE) { AstronomyPicturesScreen(modifier = Modifier.fillMaxSize()) }
            }
        }
    }
}