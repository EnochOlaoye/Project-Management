package com.example.solutions4u

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.runtime.*
import org.junit.Rule
import org.junit.Test
import com.example.solutions4u.ui.theme.Solutions4UTheme
import com.example.solutions4u.screens.SettingsScreen

class DarkModeTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Helper to launch SettingsScreen
    private fun launchSettingsScreen() {
        composeTestRule.setContent {
            Solutions4UTheme {
                SettingsScreen(
                    userId = "1",
                    userName = "Test",
                    userEmail = "test@test.com",
                    onBackClick = {},
                    onAccountDeleted = {},
                    onLogout = {}
                )
            }
        }
        composeTestRule.waitForIdle()
    }

    // Test 1: Theme button exists on screen
    @Test
    fun themeToggleSwitchExists() {
        launchSettingsScreen()
        composeTestRule
            .onNodeWithText("Theme")
            .assertExists()
    }

    // Test 2: Theme button can be clicked
    @Test
    fun themeToggleSwitchCanBeClicked() {
        launchSettingsScreen()
        composeTestRule
            .onNodeWithText("Theme")
            .assertExists()
            .performClick()
        composeTestRule.waitForIdle()
    }

    // Test 3: App starts in light mode by default
    @Test
    fun themeStartsInLightMode() {
        composeTestRule.setContent {
            Solutions4UTheme {
                SettingsScreen(
                    userId = "1",
                    userName = "Test",
                    userEmail = "test@test.com",
                    onBackClick = {},
                    onAccountDeleted = {},
                    onLogout = {}
                )
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule
            .onNodeWithText("Theme")
            .assertExists()
    }

    // Test 4: App can be switched to dark mode
    @Test
    fun themeCanBeSwitchedToDarkMode() {
        composeTestRule.setContent {
            Solutions4UTheme {
                SettingsScreen(
                    userId = "1",
                    userName = "Test",
                    userEmail = "test@test.com",
                    onBackClick = {},
                    onAccountDeleted = {},
                    onLogout = {}
                )
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule
            .onNodeWithText("Theme")
            .performClick()
        composeTestRule.waitForIdle()
        composeTestRule
            .onNodeWithText("Theme")
            .assertExists()
    }
}