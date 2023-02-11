package com.trevorcrawford.apod.ui.astronomypicture

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * UI tests for [AstronomyPictureDetailScreen].
 */
@RunWith(AndroidJUnit4::class)
class AstronomyPictureDetailScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setup() {
        composeTestRule.setContent {
            AstronomyPictureDetailScreen(
                uiState = AstronomyPictureDetailUiState.Data(
                    TEST_DATA
                )
            )
        }
    }

    @Test
    fun astronomy_picture_data_exists() {
        composeTestRule.onNodeWithText(TEST_DATA.title).assertExists()
        composeTestRule.onNodeWithText(TEST_DATA.copyright, substring = true).assertExists()
        composeTestRule.onNodeWithText(TEST_DATA.explanation).assertExists()
        composeTestRule.onNodeWithText(
            DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).format(TEST_DATA.date)
        ).assertExists()
    }
}

private val TEST_DATA = testPreview
