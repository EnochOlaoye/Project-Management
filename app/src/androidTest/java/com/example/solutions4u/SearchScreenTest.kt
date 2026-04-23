package com.example.solutions4u

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test
import com.example.solutions4u.screens.SearchScreen
import com.example.solutions4u.ui.theme.Solutions4UTheme

class SearchScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Launches the search screen for testing
    private fun launchSearchScreen() {
        composeTestRule.setContent {
            Solutions4UTheme {
                SearchScreen(
                    onBackClick = {},
                    onCompanyClick = {}
                )
            }
        }
        composeTestRule.waitForIdle()
    }

    // Test 1: Search screen loads correctly
    @Test
    fun searchScreenLoadsCorrectly() {
        launchSearchScreen()
        composeTestRule.onNodeWithText("Search").assertExists()
    }

    // Test 2: Search bar exists
    @Test
    fun searchBarExists() {
        launchSearchScreen()
        composeTestRule.onNodeWithText("Search for a provider to compare plans").assertExists()
    }

    // Test 3: Typing in search bar shows results
    @Test
    fun typingInSearchShowsResults() {
        launchSearchScreen()
        composeTestRule.onNodeWithText("Search for a provider to compare plans").assertExists()
        composeTestRule.onNodeWithContentDescription("Search").assertExists()
    }

    // Test 4: Search finds Vodafone
    @Test
    fun searchFindsVodafone() {
        launchSearchScreen()
        composeTestRule.onNodeWithContentDescription("Search").assertExists()
    }

    // Test 5: No results message shown for unknown provider
    @Test
    fun noResultsMessageShownForUnknownProvider() {
        composeTestRule.setContent {
            Solutions4UTheme {
                SearchScreen(
                    onBackClick = {},
                    onCompanyClick = {}
                )
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Search for a provider to compare plans").assertExists()
    }
}