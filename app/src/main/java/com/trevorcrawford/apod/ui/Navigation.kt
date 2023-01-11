package com.trevorcrawford.apod.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.trevorcrawford.apod.ui.astronomypicture.AstronomyPicturesScreen

@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main") {
        composable("main") { AstronomyPicturesScreen(modifier = Modifier.fillMaxSize()) }
        // TODO: Add more destinations
    }
}
