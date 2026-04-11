package com.example.solutions4u

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test
import com.example.solutions4u.screens.ProfileScreen
import com.example.solutions4u.ui.theme.Solutions4UTheme

class SwipeTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun allTabsExist() {
        composeTestRule.setContent {
            Solutions4UTheme {
                ProfileScreen(
                    userId = "1",
                    userName = "Test",
                    userEmail = "test@email.com",
                    onBackClick = {},
                    onSettingsClick = {}
                )
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("tab_Daily").assertExists()
        composeTestRule.onNodeWithTag("tab_Weekly").assertExists()
        composeTestRule.onNodeWithTag("tab_Monthly").assertExists()
        composeTestRule.onNodeWithTag("tab_Yearly").assertExists()
    }

    @Test
    fun dailyTabCanBeClicked() {
        composeTestRule.setContent {
            Solutions4UTheme {
                ProfileScreen(
                    userId = "1",
                    userName = "Test",
                    userEmail = "test@email.com",
                    onBackClick = {},
                    onSettingsClick = {}
                )
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("tab_Daily").performClick()
        composeTestRule.onNodeWithTag("tab_Daily").assertExists()
    }

    @Test
    fun weeklyTabCanBeClicked() {
        composeTestRule.setContent {
            Solutions4UTheme {
                ProfileScreen(
                    userId = "1",
                    userName = "Test",
                    userEmail = "test@email.com",
                    onBackClick = {},
                    onSettingsClick = {}
                )
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("tab_Weekly").performClick()
        composeTestRule.onNodeWithTag("tab_Weekly").assertExists()
    }

    @Test
    fun monthlyTabCanBeClicked() {
        composeTestRule.setContent {
            Solutions4UTheme {
                ProfileScreen(
                    userId = "1",
                    userName = "Test",
                    userEmail = "test@email.com",
                    onBackClick = {},
                    onSettingsClick = {}
                )
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("tab_Monthly").performClick()
        composeTestRule.onNodeWithTag("tab_Monthly").assertExists()
    }

    @Test
    fun yearlyTabCanBeClicked() {
        composeTestRule.setContent {
            Solutions4UTheme {
                ProfileScreen(
                    userId = "1",
                    userName = "Test",
                    userEmail = "test@email.com",
                    onBackClick = {},
                    onSettingsClick = {}
                )
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("tab_Yearly").performClick()
        composeTestRule.onNodeWithTag("tab_Yearly").assertExists()
    }
}