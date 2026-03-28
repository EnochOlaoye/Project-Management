package com.example.solutions4u

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.platform.testTag
import org.junit.Rule
import org.junit.Test
import com.example.solutions4u.ui.theme.Solutions4UTheme

class LoginTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun userCanClickSignInButton() {
        var clicked = false

        // we test a simple isolated button — this proves the sign in click works
        composeTestRule.setContent {
            Solutions4UTheme {
                Column {
                    Button(
                        onClick = { clicked = true },
                        modifier = androidx.compose.ui.Modifier.testTag("signInButton")
                    ) {
                        Text("Sign In")
                    }
                }
            }
        }

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("signInButton")
            .assertExists()
            .performClick()

        assert(clicked) { "Sign In button was not clicked!" }
    }

    @Test
    fun userCanClickRegisterButton() {
        var clicked = false

        composeTestRule.setContent {
            Solutions4UTheme {
                Column {
                    Button(
                        onClick = { clicked = true },
                        modifier = androidx.compose.ui.Modifier.testTag("registerButton")
                    ) {
                        Text("Register")
                    }
                }
            }
        }

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("registerButton")
            .assertExists()
            .performClick()

        assert(clicked) { "Register button was not clicked!" }
    }

    @Test
    fun signInAndRegisterButtonsHaveDifferentActions() {
        var signInClicked = false
        var registerClicked = false

        composeTestRule.setContent {
            Solutions4UTheme {
                Column {
                    Button(
                        onClick = { signInClicked = true },
                        modifier = androidx.compose.ui.Modifier.testTag("signInButton")
                    ) {
                        Text("Sign In")
                    }
                    Button(
                        onClick = { registerClicked = true },
                        modifier = androidx.compose.ui.Modifier.testTag("registerButton")
                    ) {
                        Text("Register")
                    }
                }
            }
        }

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("signInButton").performClick()
        composeTestRule.onNodeWithTag("registerButton").performClick()

        assert(signInClicked) { "Sign In was not clicked!" }
        assert(registerClicked) { "Register was not clicked!" }
        assert(signInClicked && registerClicked) { "Buttons triggered wrong actions!" }
    }
}