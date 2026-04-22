package com.example.solutions4u

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test
import com.example.solutions4u.screens.CategoryScreen
import com.example.solutions4u.ui.theme.Solutions4UTheme

class CompareTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Helper to launch CategoryScreen
    private fun launchCategoryScreen(categoryName: String = "Electricity") {
        composeTestRule.setContent {
            Solutions4UTheme {
                CategoryScreen(
                    categoryName = categoryName,
                    onBackClick = {}
                )
            }
        }
        composeTestRule.waitForIdle()
    }

    // Test 1: Category screen loads correctly
    @Test
    fun categoryScreenLoadsCorrectly() {
        launchCategoryScreen()
        composeTestRule.onAllNodesWithText("Electricity")[0].assertExists()
    }

    // Test 2: Compare Now button exists
    @Test
    fun compareNowButtonExists() {
        launchCategoryScreen()
        composeTestRule.onNodeWithTag("compareNowButton").assertExists()
    }

    // Test 3: Clicking Compare Now shows the compare view
    @Test
    fun compareNowButtonOpensCompareView() {
        launchCategoryScreen()
        composeTestRule.onNodeWithTag("compareNowButton").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("comparePlansView").assertExists()
    }

    // Test 4: Compare view shows the correct heading
    @Test
    fun compareViewShowsHeading() {
        launchCategoryScreen()
        composeTestRule.onNodeWithTag("compareNowButton").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Compare Electricity Plans").assertExists()
    }

    // Test 5: Current plan card exists in compare view
    @Test
    fun currentPlanIsShown() {
        launchCategoryScreen()
        composeTestRule.onNodeWithTag("compareNowButton").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Current Plan").assertExists()
    }

    // Test 6: Back button exists in compare view
    @Test
    fun backButtonExistsInCompareView() {
        launchCategoryScreen()
        composeTestRule.onNodeWithTag("compareNowButton").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("comparePlansView").assertExists()
    }

    // Test 7: Back button returns to category screen
    @Test
    fun backButtonReturnsToCategory() {
        launchCategoryScreen()
        composeTestRule.onNodeWithTag("compareNowButton").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("comparePlansView").assertExists()
        composeTestRule.onNodeWithTag("compareNowButton").assertDoesNotExist()
    }

    // Test 8: Compare works for Gas category
    @Test
    fun compareWorksForGas() {
        launchCategoryScreen("Gas")
        composeTestRule.onNodeWithTag("compareNowButton").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Compare Gas Plans").assertExists()
    }

    // Test 9: Compare works for Broadband category
    @Test
    fun compareWorksForBroadband() {
        launchCategoryScreen("Broadband")
        composeTestRule.onNodeWithTag("compareNowButton").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Compare Broadband Plans").assertExists()
    }

    // Test 10: Compare works for Mobile category
    @Test
    fun compareWorksForMobile() {
        launchCategoryScreen("Mobile")
        composeTestRule.onNodeWithTag("compareNowButton").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Compare Mobile Plans").assertExists()
    }
}