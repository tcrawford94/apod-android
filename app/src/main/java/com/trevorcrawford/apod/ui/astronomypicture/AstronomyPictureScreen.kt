package com.trevorcrawford.apod.ui.astronomypicture

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.trevorcrawford.apod.R
import com.trevorcrawford.apod.ui.astronomypicture.model.AstronomyPicturePreview
import com.trevorcrawford.apod.ui.theme.ApodIcons
import com.trevorcrawford.apod.ui.theme.ApodTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun AstronomyPictureScreen(
    modifier: Modifier = Modifier,
    viewModel: AstronomyPictureViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    AstronomyPictureScreen(
        uiState = uiState,
        onChangeSortClick = viewModel::changeSortOption,
        modifier = modifier
    )
}

@Composable
internal fun AstronomyPictureScreen(
    uiState: AstronomyPictureUiState,
    onChangeSortClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) {
        when (uiState) {
            is AstronomyPictureUiState.Data -> {
                ApodList(
                    previewList = uiState.previewList,
                    sortOrderDescription = stringResource(id = uiState.sortOrderRes),
                    onChangeSortClick = onChangeSortClick
                )
            }
            else -> {
                Text("UiState is not Success")
            }
        }

    }
}

@Composable
private fun ApodList(
    previewList: List<AstronomyPicturePreview>,
    sortOrderDescription: String,
    onChangeSortClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val gradientHeight = 56.dp

    Box(modifier) {
        LazyColumn(
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 8.dp,
                bottom = gradientHeight
            )
        ) {
            items(previewList) { preview ->
                ApodPreviewRow(
                    preview,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(gradientHeight)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.onBackground
                        )
                    )
                )
        )
        SortFAB(
            sortOrderDescription = sortOrderDescription,
            onClick = onChangeSortClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        )
    }
}

@Composable
private fun ApodPreviewRow(
    uiState: AstronomyPicturePreview,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = uiState.thumbnailUrl,
                modifier = Modifier
                    .size(100.dp)
                    .padding(12.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                placeholder = rememberVectorPainter(image = ApodIcons.Stars)
            )
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(top = 12.dp, bottom = 12.dp, end = 12.dp)
            ) {
                Text(
                    text = uiState.title,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2
                )
                Text(
                    text = LongDateFormatter.format(uiState.date),
                    modifier = Modifier.paddingFromBaseline(16.dp),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@Composable
private fun SortFAB(
    sortOrderDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        shape = CircleShape
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(imageVector = ApodIcons.Sort, contentDescription = null)
            Text(
                text = stringResource(R.string.sorted_by, sortOrderDescription),
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}

private val LongDateFormatter by lazy {
    DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
}

// Previews
@Preview(showBackground = true, widthDp = 375)
@Composable
private fun RowPreview() {
    ApodTheme {
        ApodPreviewRow(
            uiState = testPreviews[1],
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 875)
@Composable
private fun PortraitPreview() {
    ApodTheme {
        AstronomyPictureScreen(
            uiState = AstronomyPictureUiState.Data(
                previewList = testPreviews,
                sortOrderRes = R.string.title
            ),
            onChangeSortClick = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 875, heightDp = 375)
@Composable
private fun LandscapePreview() {
    ApodTheme {
        AstronomyPictureScreen(
            uiState = AstronomyPictureUiState.Data(
                previewList = testPreviews,
                sortOrderRes = R.string.date
            ),
            onChangeSortClick = {}
        )
    }
}

private val testPreviews = listOf(
    AstronomyPicturePreview(
        title = "The Milky Way",
        date = LocalDate.now(),
        thumbnailUrl = "https://apod.nasa.gov/apod/image/0712/ic1396_wood.jpg",
    ),
    AstronomyPicturePreview(
        title = "Orion's Belt Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt",
        date = LocalDate.MIN,
        thumbnailUrl = "https://apod.nasa.gov/apod/image/0712/ic1396_wood.jpg",
    ),
    AstronomyPicturePreview(
        title = "Full Moon",
        date = LocalDate.MAX,
        thumbnailUrl = "https://apod.nasa.gov/apod/image/0712/ic1396_wood.jpg",
    )
)
