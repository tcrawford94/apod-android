package com.trevorcrawford.apod.ui.util

import com.trevorcrawford.apod.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SnackbarManagerTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var snackbarManager: SnackbarManager

    @Before
    fun initialize() {
        snackbarManager = SnackbarManager()
    }

    @Test
    fun messages_initially_empty() = runTest {
        assertEquals(listOf<Message>(), snackbarManager.messages.value)
    }

    @Test
    fun showMessage_adds_message_to_messages_list() = runTest {
        snackbarManager.showMessage(1)
        assertEquals(1, snackbarManager.messages.value.size)
    }

    @Test
    fun showMessage_adds_multiple_messages_and_setMessageShown_removes_message() = runTest {
        snackbarManager.showMessage(testMessage1MessageId)
        snackbarManager.showMessage(testMessage2MessageId)

        var messages = snackbarManager.messages.value
        assertEquals(2, messages.size)
        assertEquals(testMessage1MessageId, messages[0].stringResId)

        snackbarManager.setMessageShown(messages[0].id)
        messages = snackbarManager.messages.value

        assertEquals(1, messages.size)
        assertEquals(testMessage2MessageId, messages[0].stringResId)

        snackbarManager.setMessageShown(messages[0].id)
        messages = snackbarManager.messages.value

        assertEquals(0, messages.size)
    }

    private val testMessage1MessageId = 12341234
    private val testMessage2MessageId = 98769876
}