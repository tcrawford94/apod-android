package com.trevorcrawford.apod.ui.astronomypicture

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.trevorcrawford.apod.R
import com.trevorcrawford.apod.data.AstronomyPicture
import com.trevorcrawford.apod.ui.theme.ApodIcons
import com.trevorcrawford.apod.ui.theme.ApodTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun AstronomyPictureDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: AstronomyPictureDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    AstronomyPictureDetailScreen(uiState = uiState, modifier = modifier)
}

@Composable
internal fun AstronomyPictureDetailScreen(
    uiState: AstronomyPictureDetailUiState,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        AstronomyPictureDetailUiState.Loading -> Text("Loading") //TODO
        is AstronomyPictureDetailUiState.Error -> Text("Error") //TODO
        is AstronomyPictureDetailUiState.Data ->
            PictureDetailContent(astronomyPicture = uiState.astronomyPicture, modifier = modifier)
    }
}

@Composable
private fun PictureDetailContent(
    astronomyPicture: AstronomyPicture,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(
                rememberScrollState(),
                enabled = true
            )
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = astronomyPicture.hdUrl,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp)),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            placeholder = rememberVectorPainter(image = ApodIcons.Stars)
        )
        Text(
            text = astronomyPicture.title,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
                .format(astronomyPicture.date),
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodySmall,
        )
        Text(
            text = astronomyPicture.explanation,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodyLarge
        )
        if (astronomyPicture.copyright.isNotBlank()) {
            Text(
                text = stringResource(
                    id = R.string.copyright,
                    astronomyPicture.copyright
                ),
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

// Previews
@Preview(showBackground = true, widthDp = 375, heightDp = 875)
@Composable
private fun PortraitPreview() {
    ApodTheme {
        AstronomyPictureDetailScreen(
            uiState = AstronomyPictureDetailUiState.Data(testPreview)
        )
    }
}

private val testPreview = AstronomyPicture(
    title = "Europa: Oceans of Life?",
    explanation = "Is there life beneath Europa's frozen surface? Some believe the oceans found there of carbon-enriched water are the best chance for life, outside the Earth, in our Solar System. Europa, the fourth largest moon of Jupiter, was recently discovered to have a thin oxygen atmosphere by scientists using the Hubble Space Telescope. Although Earth's atmospheric abundance of oxygen is indicative of life, astronomers speculate that Europa's oxygen arises purely from physical processes. But what an interesting coincidence! The above picture was taken by a Voyager spacecraft in 1979, but the spacecraft Galileo is currently circling Jupiter and has been photographing Europa. The first of these pictures will be released two days from today. Will they show the unexpected?  Late News: NASA to Release New Europa and Jupiter Pictures Thursday  More Late News: New Evidence Suggests Microscopic Life Existed on Mars",
    date = LocalDate.parse("1996-08-06"),
    hdUrl = "https://apod.nasa.gov/apod/image/1706/Summer2017Sky_universe2go_2500.jpg",
    url = "https://apod.nasa.gov/apod/image/1706/Summer2017Sky_universe2go_960.jpg",
    copyright = "Test Copyright"
)
