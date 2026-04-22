package com.example.solutions4u

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test
import com.example.solutions4u.screens.ProfileScreen
import com.example.solutions4u.ui.theme.Solutions4UTheme

class BillDueDateTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Helper to launch ProfileScreen
    private fun launchProfileScreen() {
        composeTestRule.setContent {
            Solutions4UTheme {
                ProfileScreen(
                    userId = "1",
                    userName = "Test User",
                    userEmail = "test@test.com",
                    onBackClick = {},
                    onSettingsClick = {}
                )
            }
        }
        composeTestRule.waitForIdle()
    }

    // Test 1: Upcoming charges section exists
    @Test
    fun upcomingChargesSectionExists() {
        launchProfileScreen()
        composeTestRule.onNodeWithTag("upcomingBillsSection").assertExists()
    }

    // Test 2: Upcoming charges heading is visible
    @Test
    fun upcomingChargesHeadingVisible() {
        launchProfileScreen()
        composeTestRule.onNodeWithText("Upcoming Charges").assertExists()
    }

    // Test 3: First upcoming bill exists
    @Test
    fun firstUpcomingBillExists() {
        launchProfileScreen()
        composeTestRule.onNodeWithTag("upcomingBill_1").assertExists()
    }

    // Test 4: Bill category is shown
    @Test
    fun billCategoryIsShown() {
        launchProfileScreen()
        composeTestRule.onNodeWithText("Electricity").assertExists()
    }

    // Test 5: Bill provider is shown
    @Test
    fun billProviderIsShown() {
        launchProfileScreen()
        composeTestRule.onNodeWithText("Electric Ireland").assertExists()
    }

    // Test 6: Dashboard heading still exists
    @Test
    fun dashboardHeadingExists() {
        launchProfileScreen()
        composeTestRule.onNodeWithText("Dashboard").assertExists()
    }

    // Test 7: Tabs still exist alongside upcoming charges
    @Test
    fun tabsStillExist() {
        launchProfileScreen()
        composeTestRule.onNodeWithTag("tab_Daily").assertExists()
        composeTestRule.onNodeWithTag("tab_Weekly").assertExists()
        composeTestRule.onNodeWithTag("tab_Monthly").assertExists()
        composeTestRule.onNodeWithTag("tab_Yearly").assertExists()
    }
}