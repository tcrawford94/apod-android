package com.trevorcrawford.apod.ui.astronomypicture

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

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
                onRefresh = {},
                onPreviewClicked = {}
            )
        }
    }

    @Test
    fun firstItem_exists() {
        composeTestRule.onNodeWithText(TEST_DATA.first().title).assertExists().performClick()
    }
}

private val TEST_DATA = testPreviews
