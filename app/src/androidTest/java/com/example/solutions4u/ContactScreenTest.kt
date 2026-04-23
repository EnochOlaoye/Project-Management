package com.example.solutions4u

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test
import com.example.solutions4u.screens.ContactScreen
import com.example.solutions4u.ui.theme.Solutions4UTheme

class ContactScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Launches the contact screen for testing
    private fun launchContactScreen() {
        composeTestRule.setContent {
            Solutions4UTheme {
                ContactScreen(onBackClick = {})
            }
        }
        composeTestRule.waitForIdle()
    }

    // Test 1: Contact screen loads correctly
    @Test
    fun contactScreenLoadsCorrectly() {
        launchContactScreen()
        composeTestRule.onNodeWithText("Contact Us").assertExists()
    }

    // Test 2: Get in Touch heading exists
    @Test
    fun getInTouchHeadingExists() {
        launchContactScreen()
        composeTestRule.onNodeWithText("Get in Touch").assertExists()
    }

    // Test 3: Phone number is displayed
    @Test
    fun phoneNumberIsDisplayed() {
        launchContactScreen()
        composeTestRule.onNodeWithText("+353 123 123 123").assertExists()
    }

    // Test 4: Email address is displayed
    @Test
    fun emailAddressIsDisplayed() {
        launchContactScreen()
        composeTestRule.onNodeWithText("support@solution4u.ie").assertExists()
    }

    // Test 5: Address is displayed
    @Test
    fun addressIsDisplayed() {
        launchContactScreen()
        composeTestRule.onNodeWithText("123 Example Street").assertExists()
    }

    // Test 6: Business hours section exists
    @Test
    fun businessHoursExists() {
        launchContactScreen()
        composeTestRule.onNodeWithText("Business Hours").assertExists()
    }
}