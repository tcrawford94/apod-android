package com.trevorcrawford.apod.ui.astronomypicture

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.pullRefreshIndicatorTransform
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun AstronomyPicturesScreen(
    onNavigateToPictureDetail: (date: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AstronomyPicturesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    AstronomyPicturesScreen(
        uiState = uiState,
        onChangeSortOption = viewModel::changeSortOption,
        onRefresh = viewModel::loadPictures,
        onPreviewClicked = { onNavigateToPictureDetail(it.date.toString()) },
        modifier = modifier
    )
}

@Composable
internal fun AstronomyPicturesScreen(
    uiState: AstronomyPicturesUiState,
    onChangeSortOption: () -> Unit,
    onRefresh: () -> Unit,
    onPreviewClicked: (preview: AstronomyPicturePreview) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) {
        when (uiState) {
            is AstronomyPicturesUiState.Data -> {
                AstronomyPicturePreviewList(
                    previewList = uiState.previewList,
                    sortOrderDescription = stringResource(id = uiState.sortOrderRes),
                    onChangeSortOption = onChangeSortOption,
                    refreshing = uiState.isRefreshing,
                    onRefresh = onRefresh,
                    onPreviewClicked = onPreviewClicked
                )
            }
            is AstronomyPicturesUiState.Error -> {
                // 
                Text(
                    uiState.throwable.localizedMessage
                        ?: stringResource(id = R.string.error_unable_to_load_pictures)
                )
            }
        }

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun AstronomyPicturePreviewList(
    previewList: ImmutableList<AstronomyPicturePreview>,
    sortOrderDescription: String,
    onChangeSortOption: () -> Unit,
    refreshing: Boolean,
    onRefresh: () -> Unit,
    onPreviewClicked: (preview: AstronomyPicturePreview) -> Unit,
    modifier: Modifier = Modifier
) {
    val pullRefreshState = rememberPullRefreshState(refreshing, onRefresh)
    Box(modifier.pullRefresh(pullRefreshState)) {
        LazyColumn(
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 8.dp,
                bottom = 56.dp // Don't overlap too much with FAB
            ),
            modifier = Modifier.fillMaxSize()
        ) {
            items(previewList) { preview ->
                ApodPreviewRow(
                    astronomyPicturePreview = preview,
                    onClick = { onPreviewClicked(preview) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }
        }
        if (previewList.isNotEmpty()) {
            SortFAB(
                sortOrderDescription = sortOrderDescription,
                onClick = onChangeSortOption,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp)
            )
        }
        PullRefreshIndicator(
            refreshing = refreshing,
            state = pullRefreshState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .pullRefreshIndicatorTransform(pullRefreshState)
                .padding(top = if (pullRefreshState.progress > 0 || refreshing) 32.dp else 0.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ApodPreviewRow(
    astronomyPicturePreview: AstronomyPicturePreview,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = astronomyPicturePreview.thumbnailUrl,
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
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 12.dp, end = 12.dp)
                    .weight(1f)
            ) {
                Text(
                    text = astronomyPicturePreview.title,
                    modifier = Modifier.fillMaxWidth(),
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2
                )
                Text(
                    text = LongDateFormatter.format(astronomyPicturePreview.date),
                    modifier = Modifier
                        .fillMaxWidth()
                        .paddingFromBaseline(16.dp),
                    style = MaterialTheme.typography.labelSmall
                )
                if (astronomyPicturePreview.copyright.isNotBlank()) {
                    Text(
                        text = stringResource(
                            id = R.string.copyright,
                            astronomyPicturePreview.copyright
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .paddingFromBaseline(16.dp),
                        style = MaterialTheme.typography.bodySmall,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
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
            astronomyPicturePreview = testPreviews[1],
            onClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 875)
@Composable
private fun PortraitPreview() {
    ApodTheme {
        AstronomyPicturesScreen(
            uiState = AstronomyPicturesUiState.Data(
                previewList = testPreviews,
                sortOrderRes = R.string.title,
                isRefreshing = false
            ),
            onChangeSortOption = {},
            onRefresh = {},
            onPreviewClicked = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 875, heightDp = 375)
@Composable
private fun LandscapePreview() {
    ApodTheme {
        AstronomyPicturesScreen(
            uiState = AstronomyPicturesUiState.Data(
                previewList = testPreviews,
                sortOrderRes = R.string.date,
                isRefreshing = false
            ),
            onChangeSortOption = {},
            onRefresh = {},
            onPreviewClicked = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 875)
@Composable
private fun LoadingPreview() {
    ApodTheme {
        AstronomyPicturesScreen(
            uiState = AstronomyPicturesUiState.Data(
                previewList = persistentListOf(),
                sortOrderRes = R.string.date,
                isRefreshing = true
            ),
            onChangeSortOption = {},
            onRefresh = {},
            onPreviewClicked = {}
        )
    }
}

val testPreviews = persistentListOf(
    AstronomyPicturePreview(
        title = "The Milky Way",
        date = LocalDate.now(),
        thumbnailUrl = "https://apod.nasa.gov/apod/image/0712/ic1396_wood.jpg",
        copyright = "Ricky Bobby, if you ain't first you're last"
    ),
    AstronomyPicturePreview(
        title = "Orion's Belt Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt",
        date = LocalDate.MIN,
        thumbnailUrl = "https://apod.nasa.gov/apod/image/0712/ic1396_wood.jpg",
        copyright = "Freddie Kruger"
    ),
    AstronomyPicturePreview(
        title = "Full Moon",
        date = LocalDate.MAX,
        thumbnailUrl = "https://apod.nasa.gov/apod/image/0712/ic1396_wood.jpg",
        copyright = "Eddie Munster"
    )
)
