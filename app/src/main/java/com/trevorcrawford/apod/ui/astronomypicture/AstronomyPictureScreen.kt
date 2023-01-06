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
import androidx.compose.ui.text.font.FontWeight
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

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun AstronomyPictureScreen(
    modifier: Modifier = Modifier,
    viewModel: AstronomyPictureViewModel = hiltViewModel()
) {
    val items by viewModel.uiState.collectAsStateWithLifecycle()
    if (items is AstronomyPictureUiState.Success) {
        AstronomyPictureScreen(
            items = (items as AstronomyPictureUiState.Success).data,
            //onSave = viewModel::addAstronomyPicture,
            modifier = modifier
        )
    }
}

@Composable
internal fun AstronomyPictureScreen(
    items: List<AstronomyPicturePreview>,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) {
        ApodList(
            list = items,
            sortOrderDescription = "todo",
            onChangeSortClick = { /*TODO*/ }
        )
    }
}

@Composable
fun ApodList(
    list: List<AstronomyPicturePreview>,
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
            item {
                Text(
                    text = stringResource(R.string.sorted_by, sortOrderDescription),
                    modifier = Modifier.padding(vertical = 8.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            items(list) { preview ->
                ApodPreviewItem(
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
            onClick = onChangeSortClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        )
    }
}

@Composable
fun ApodPreviewItem(
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
                    .size(80.dp)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                placeholder = rememberVectorPainter(image = ApodIcons.Stars)
            )
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = uiState.title,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = uiState.subTitle,
                    modifier = Modifier.paddingFromBaseline(16.dp),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@Composable
fun SortFAB(
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
                text = stringResource(id = R.string.reorder_list),
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}

// Previews
@Preview(showBackground = true, widthDp = 375, heightDp = 875)
@Composable
private fun PortraitPreview() {
    ApodTheme {
        AstronomyPictureScreen(
            items = listOf(
                AstronomyPicturePreview(
                    title = "The Milky Way",
                    subTitle = "2022-10-23",
                    thumbnailUrl = "https://apod.nasa.gov/apod/image/0712/ic1396_wood.jpg",
                )
            )
        )
    }
}

@Preview(showBackground = true, widthDp = 875, heightDp = 375)
@Composable
private fun LandscapePreview() {
    ApodTheme {
        AstronomyPictureScreen(
            items = listOf(
                AstronomyPicturePreview(
                    title = "The Milky Way",
                    subTitle = "2022-10-23",
                    thumbnailUrl = "https://apod.nasa.gov/apod/image/0712/ic1396_wood.jpg",
                )
            )
        )
    }
}
