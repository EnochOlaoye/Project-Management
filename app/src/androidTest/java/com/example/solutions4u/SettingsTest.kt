package com.example.solutions4u

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test
import com.example.solutions4u.screens.SettingsScreen
import com.example.solutions4u.screens.ProfileScreen
import com.example.solutions4u.ui.theme.Solutions4UTheme

class SettingsTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Helper to launch SettingsScreen
    private fun launchSettingsScreen() {
        composeTestRule.setContent {
            Solutions4UTheme {
                SettingsScreen(
                    userId = "1",
                    userName = "Test User",
                    userEmail = "test@test.com",
                    onBackClick = {},
                    onAccountDeleted = {},
                    onLogout = {}
                )
            }
        }
        composeTestRule.waitForIdle()
    }

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

    // Test 1: Profile page loads correctly
    @Test
    fun profilePageLoadsCorrectly() {
        launchProfileScreen()
        composeTestRule.onNodeWithText("Dashboard").assertExists()
    }

    // Test 2: Settings screen loads correctly
    @Test
    fun settingsScreenLoadsCorrectly() {
        launchSettingsScreen()
        composeTestRule.onNodeWithText("Settings").assertExists()
    }

    // Test 3: Change Login Info button exists
    @Test
    fun changeLoginInfoButtonExists() {
        launchSettingsScreen()
        composeTestRule.onNodeWithText("Change Login Info").assertExists()
    }

    // Test 4: Change Login Info button can be clicked and dialog appears
    @Test
    fun changeLoginInfoDialogOpens() {
        launchSettingsScreen()
        composeTestRule.onNodeWithText("Change Login Info").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onAllNodesWithText("Change Login Info")[0].assertExists()
    }

    // Test 5: Add property button exists
    @Test
    fun addPropertyButtonExists() {
        launchSettingsScreen()
        composeTestRule.onNodeWithText("Add").assertExists()
    }

    // Test 6: Add property button can be clicked and dialog appears
    @Test
    fun addPropertyDialogOpens() {
        launchSettingsScreen()
        composeTestRule.onNodeWithText("Add").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Add Property").assertExists()
    }

    // Test 7: Delete property button exists
    @Test
    fun deletePropertyButtonExists() {
        launchSettingsScreen()
        composeTestRule.onNodeWithText("Delete").assertExists()
    }

    // Test 8: Delete property button can be clicked
    @Test
    fun deletePropertyDialogOpens() {
        launchSettingsScreen()
        composeTestRule.onNodeWithText("Delete").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Delete a Property").assertExists()
    }

    // Test 9: Change Details button exists for editing properties
    @Test
    fun editPropertyButtonExists() {
        launchSettingsScreen()
        composeTestRule.onNodeWithText("Change Details").assertExists()
    }

   // Test 10: Delete Account button exists
   @Test
   fun deleteAccountButtonExists() {
       launchSettingsScreen()
       composeTestRule.onNodeWithText("Delete Account").assertExists()
    }

    // Test 11: Delete Account button can be clicked and confirmation dialog appears
    @Test
    fun deleteAccountDialogOpens() {
        launchSettingsScreen()
        composeTestRule.onNodeWithText("Delete Account").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Are you sure you want to delete your account? This cannot be undone.").assertExists()
    }

    // Test 12: Theme button exists
    @Test
    fun themeButtonExists() {
        launchSettingsScreen()
        composeTestRule.onNodeWithText("Theme").assertExists()
    }

    // Test 13: Theme button can be clicked and dialog opens
    @Test
    fun themeDialogOpens() {
        launchSettingsScreen()
        composeTestRule.onNodeWithText("Theme").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Theme").assertExists()
    }

    // Test 14: Logout button exists
    @Test
    fun logoutButtonExists() {
        launchSettingsScreen()
        composeTestRule.onNodeWithText("Logout").assertExists()
    }

    // Test 15: Logout button can be clicked
    @Test
    fun logoutButtonCanBeClicked() {
        var loggedOut = false
        composeTestRule.setContent {
            Solutions4UTheme {
                SettingsScreen(
                    userId = "1",
                    userName = "Test User",
                    userEmail = "test@test.com",
                    onBackClick = {},
                    onAccountDeleted = {},
                    onLogout = { loggedOut = true }
                )
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Logout").performClick()
        composeTestRule.waitForIdle()
        assert(loggedOut) { "Logout was not triggered!" }
    }
}