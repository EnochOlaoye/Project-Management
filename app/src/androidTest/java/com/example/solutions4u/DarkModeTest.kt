package com.example.solutions4u

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.runtime.*
import org.junit.Rule
import org.junit.Test
import com.example.solutions4u.ui.theme.Solutions4UTheme
import com.example.solutions4u.screens.HomeScreen

class DarkModeTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun themeToggleSwitchExists() {
        composeTestRule.setContent {
            Solutions4UTheme {
                HomeScreen(
                    onCategoryClick = {},
                    onSignInClick = {},
                    onRegisterClick = {}
                )
            }
        }

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("themeToggle")
            .assertExists()
    }

    @Test
    fun themeToggleSwitchCanBeClicked() {
        var toggled = false

        composeTestRule.setContent {
            Solutions4UTheme {
                HomeScreen(
                    onCategoryClick = {},
                    onSignInClick = {},
                    onRegisterClick = {},
                    isDarkTheme = false,
                    onThemeToggle = { toggled = true }
                )
            }
        }

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("themeToggle")
            .assertExists()
            .performClick()

        assert(toggled) { "Theme toggle was not clicked!" }
    }

    @Test
    fun themeStartsInLightMode() {
        composeTestRule.setContent {
            Solutions4UTheme(darkTheme = false) {
                HomeScreen(
                    onCategoryClick = {},
                    onSignInClick = {},
                    onRegisterClick = {},
                    isDarkTheme = false,
                    onThemeToggle = {}
                )
            }
        }

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("themeToggle")
            .assertIsOff()
    }

    @Test
    fun themeCanBeSwitchedToDarkMode() {
        composeTestRule.setContent {
            Solutions4UTheme(darkTheme = true) {
                HomeScreen(
                    onCategoryClick = {},
                    onSignInClick = {},
                    onRegisterClick = {},
                    isDarkTheme = true,
                    onThemeToggle = {}
                )
            }
        }

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("themeToggle")
            .assertIsOn()
    }
}