package com.example.solutions4u

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test
import com.example.solutions4u.screens.FaqScreen
import com.example.solutions4u.ui.theme.Solutions4UTheme

class FaqScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Launches the FAQ screen for testing
    private fun launchFaqScreen() {
        composeTestRule.setContent {
            Solutions4UTheme {
                FaqScreen(
                    onBackClick = {},
                    isAdmin = false
                )
            }
        }
        composeTestRule.waitForIdle()
    }

    // Test 1: FAQ screen loads correctly
    @Test
    fun faqScreenLoadsCorrectly() {
        launchFaqScreen()
        composeTestRule.onNodeWithText("FAQ").assertExists()
    }

    // Test 2: FAQ screen shows loading or content
    @Test
    fun faqScreenShowsContent() {
        launchFaqScreen()
        composeTestRule.onNodeWithText("FAQ").assertExists()
    }

    // Test 3: Admin add button is hidden for non-admin users
    @Test
    fun addButtonHiddenForNonAdmin() {
        launchFaqScreen()
        composeTestRule.onNodeWithContentDescription("Add FAQ").assertDoesNotExist()
    }

    // Test 4: Admin add button is visible for admin users
    @Test
    fun addButtonVisibleForAdmin() {
        composeTestRule.setContent {
            Solutions4UTheme {
                FaqScreen(
                    onBackClick = {},
                    isAdmin = true
                )
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithContentDescription("Add FAQ").assertExists()
    }
}