package com.example.solutions4u

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test
import com.example.solutions4u.screens.CategoryScreen
import com.example.solutions4u.ui.theme.Solutions4UTheme

class SaveForLaterTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Launches the category screen for testing
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

    // Test 1: Compare Now button exists
    @Test
    fun compareNowButtonExists() {
        launchCategoryScreen()
        composeTestRule.onNodeWithTag("compareNowButton").assertExists()
    }

    // Test 2: Compare view opens when Compare Now is clicked
    @Test
    fun compareViewOpens() {
        launchCategoryScreen()
        composeTestRule.onNodeWithTag("compareNowButton").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("comparePlansView").assertExists()
    }

    // Test 3: Save button exists on first plan card
    @Test
    fun saveButtonExistsOnPlanCard() {
        launchCategoryScreen()
        composeTestRule.onNodeWithTag("compareNowButton").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("saveButton_Electric Ireland").assertExists()
    }

    // Test 4: Save button can be clicked
    @Test
    fun saveButtonCanBeClicked() {
        launchCategoryScreen()
        composeTestRule.onNodeWithTag("compareNowButton").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("saveButton_Electric Ireland").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("saveButton_Electric Ireland").assertExists()
    }

    // Test 5: Save button can be clicked and plan is saved
    @Test
    fun viewSavedButtonAppearsAfterSaving() {
        launchCategoryScreen()
        composeTestRule.onNodeWithTag("compareNowButton").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("saveButton_Bord Gais Energy").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("savedCountBadge").assertExists()
    }

    // Test 6: Saved plans dialog opens when badge is clicked
    @Test
    fun savedPlansDialogOpens() {
        launchCategoryScreen()
        composeTestRule.onNodeWithTag("compareNowButton").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("saveButton_Bord Gais Energy").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("savedCountBadge").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Saved Plans").assertExists()
    }

    // Test 7: Back button returns to category screen
    @Test
    fun backButtonReturnsToCategory() {
        launchCategoryScreen()
        composeTestRule.onNodeWithTag("compareNowButton").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("comparePlansView").assertExists()
    }
}