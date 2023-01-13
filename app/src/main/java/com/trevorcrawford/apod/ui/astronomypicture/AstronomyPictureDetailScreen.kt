package com.trevorcrawford.apod.ui.astronomypicture

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun AstronomyPictureDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: AstronomyPictureDetailViewModel = hiltViewModel()
) {
    val pictureDetail by viewModel.pictureDetail.collectAsStateWithLifecycle()
    Text(text = pictureDetail.toString(), modifier = modifier)
}