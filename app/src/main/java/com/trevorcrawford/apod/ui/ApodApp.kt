package com.trevorcrawford.apod.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.trevorcrawford.apod.ui.MainDestinations.APOD_DATE_KEY
import com.trevorcrawford.apod.ui.MainDestinations.APOD_DETAIL_ROUTE
import com.trevorcrawford.apod.ui.MainDestinations.RANDOM_APOD_ROUTE
import com.trevorcrawford.apod.ui.astronomypicture.AstronomyPictureDetailScreen
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
                startDestination = RANDOM_APOD_ROUTE,
                modifier = Modifier.padding(innerPaddingModifier)
            ) {
                composable(RANDOM_APOD_ROUTE) {
                    AstronomyPicturesScreen(
                        onNavigateToPictureDetail = { date ->
                            appState.navController.navigate("${APOD_DETAIL_ROUTE}/$date")
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
                composable(
                    route = "${APOD_DETAIL_ROUTE}/{${APOD_DATE_KEY}}",
                    arguments = listOf(navArgument(APOD_DATE_KEY) { type = NavType.StringType })
                ) {
                    AstronomyPictureDetailScreen(modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}