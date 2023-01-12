package com.trevorcrawford.apod.ui

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.trevorcrawford.apod.R
import com.trevorcrawford.apod.data.di.fakeAstronomyPictures
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
        // TODO: Add more navigation tests
        composeTestRule.onNodeWithText(fakeAstronomyPictures.first().title, substring = true).assertExists()
    }

    @Test
    fun snackbarManager_show_message_displays_snackbar() {
        // when
        snackbarManager.showMessage(R.string.error_unable_to_load_pictures)

        // then
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.error_unable_to_load_pictures)).assertExists()
    }
}

