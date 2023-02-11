package com.trevorcrawford.apod.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.trevorcrawford.apod.R
import com.trevorcrawford.apod.data.fake.fakeAstronomyPictures
import com.trevorcrawford.apod.ui.util.SnackbarManager
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class NavigationTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var snackbarManager: SnackbarManager

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun main_activity_launches_to_astronomy_pictures_screen() {
        // When main activity launches - do nothing...

        // Verify titles should be shown in rows
        fakeAstronomyPictures.forEach {
            composeTestRule.onNodeWithText(it.title, substring = true).assertExists()
        }

        // Verify explanation should not be shown (too long for preview/summary level format)
        composeTestRule.onNodeWithText(fakeAstronomyPictures[1].explanation, substring = true)
            .assertDoesNotExist()
    }

    @Test
    fun main_activity_clicking_apod_row_launches_to_astronomy_picture_detail_screen() {
        // When an astronomy picture row is selected
        val selectedPic = fakeAstronomyPictures[0]
        composeTestRule.onNodeWithText(selectedPic.title, substring = true)
            .assertIsDisplayed()
            .performClick()

        // Verify that the correct astronomy picture detail screen is shown...
        composeTestRule.onNodeWithText(selectedPic.copyright, substring = true).assertExists()
        composeTestRule.onNodeWithText(selectedPic.title).assertExists()
        composeTestRule.onNodeWithText(selectedPic.explanation).assertExists()

        // And that different picture info isn't displayed
        composeTestRule.onNodeWithText(fakeAstronomyPictures[1].title, substring = true)
            .assertDoesNotExist()
    }

    @Test
    fun snackbarManager_show_message_displays_snackbar() {
        // When we tell the snackbarManager to showMessage,
        snackbarManager.showMessage(R.string.error_unable_to_load_pictures)

        // Then the message string is displayed.
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.error_unable_to_load_pictures))
            .assertExists()
    }
}

