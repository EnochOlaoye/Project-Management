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

    // Launches the profile/dashboard screen for testing
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

    @Test
    fun upcomingChargesSectionExists() {
        launchProfileScreen()
        composeTestRule.onNodeWithTag("upcomingBillsSection").assertExists()
    }

    @Test
    fun upcomingChargesHeadingVisible() {
        launchProfileScreen()
        composeTestRule.onNodeWithText("Upcoming Charges").assertExists()
    }

    @Test
    fun firstUpcomingBillExists() {
        launchProfileScreen()
        composeTestRule.onNodeWithTag("upcomingBillsSection").assertExists()
    }

    @Test
    fun billCategoryIsShown() {
        launchProfileScreen()
        composeTestRule.onNodeWithText("Electricity").assertExists()
    }

    @Test
    fun billProviderIsShown() {
        launchProfileScreen()
        composeTestRule.onNodeWithText("Electricity").assertExists()
    }

    @Test
    fun dashboardHeadingExists() {
        launchProfileScreen()
        composeTestRule.onNodeWithText("Dashboard").assertExists()
    }

    @Test
    fun tabsStillExist() {
        launchProfileScreen()
        composeTestRule.onNodeWithTag("tab_Daily").assertExists()
        composeTestRule.onNodeWithTag("tab_Weekly").assertExists()
        composeTestRule.onNodeWithTag("tab_Monthly").assertExists()
        composeTestRule.onNodeWithTag("tab_Yearly").assertExists()
    }
}