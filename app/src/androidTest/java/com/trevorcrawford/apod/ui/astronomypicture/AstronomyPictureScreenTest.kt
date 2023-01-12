package com.trevorcrawford.apod.ui.astronomypicture

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.trevorcrawford.apod.ui.astronomypicture.model.AstronomyPicturePreview
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

/**
 * UI tests for [AstronomyPicturesScreen].
 */
@RunWith(AndroidJUnit4::class)
class AstronomyPictureScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setup() {
        composeTestRule.setContent {
            AstronomyPicturesScreen(
                uiState = AstronomyPicturesUiState.Data(
                    previewList = TEST_DATA,
                    sortOrderRes = AstronomyPicturesViewModel.availableSortOptions.first().titleRes,
                    isRefreshing = false
                ),
                onChangeSortOption = {},
                onRefresh = {}
            )
        }
    }

    @Test
    fun firstItem_exists() {
        composeTestRule.onNodeWithText(TEST_DATA.first().title).assertExists().performClick()
    }
}

private val TEST_DATA = listOf(
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
