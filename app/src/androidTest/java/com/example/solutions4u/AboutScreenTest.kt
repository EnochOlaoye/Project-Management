package com.example.solutions4u

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test
import com.example.solutions4u.screens.AboutScreen
import com.example.solutions4u.ui.theme.Solutions4UTheme

class AboutScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Launches the about screen for testing
    private fun launchAboutScreen() {
        composeTestRule.setContent {
            Solutions4UTheme {
                AboutScreen(onBackClick = {})
            }
        }
        composeTestRule.waitForIdle()
    }

    // Test 1: About screen loads correctly
    @Test
    fun aboutScreenLoadsCorrectly() {
        launchAboutScreen()
        composeTestRule.onNodeWithText("About").assertExists()
    }

    // Test 2: Our Story heading exists
    @Test
    fun ourStoryHeadingExists() {
        launchAboutScreen()
        composeTestRule.onNodeWithText("Our Story").assertExists()
    }
}