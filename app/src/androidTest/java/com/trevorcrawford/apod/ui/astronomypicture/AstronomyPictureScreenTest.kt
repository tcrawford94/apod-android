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
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

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

    @Test
    fun astronomy_picture_rows_display_title_date_and_copyright() {
        val longDateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
        // Verify titles should be shown in rows
        TEST_DATA.forEach {
            composeTestRule.onNodeWithText(it.title, substring = true).assertExists()
            composeTestRule.onNodeWithText(it.copyright, substring = true).assertExists()
            composeTestRule.onNodeWithText(longDateFormatter.format(it.date), substring = true)
                .assertExists()
        }
    }
}

private val TEST_DATA = testPreviews
