package eu.slickbot.vremedo.composable

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import eu.slickbot.vremedo.theme.VremedoTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ErrorMessageTest {

  @get:Rule
  val composeTestRule = createComposeRule()

  @Test
  fun showsDefaultMessage() {
    composeTestRule.setContent {
      VremedoTheme {
        ErrorMessage()
      }
    }

    composeTestRule.onNodeWithText("Something went wrong").assertIsDisplayed()
  }

  @Test
  fun showsCustomMessage() {
    composeTestRule.setContent {
      VremedoTheme {
        ErrorMessage(message = "No internet connection")
      }
    }

    composeTestRule.onNodeWithText("No internet connection").assertIsDisplayed()
  }

  @Test
  fun hidesRetryButtonWhenNoCallback() {
    composeTestRule.setContent {
      VremedoTheme {
        ErrorMessage(onRetry = null)
      }
    }

    composeTestRule.onNodeWithText("Retry").assertDoesNotExist()
  }

  @Test
  fun retryButtonInvokesCallback() {
    var retried = false
    composeTestRule.setContent {
      VremedoTheme {
        ErrorMessage(onRetry = { retried = true })
      }
    }

    composeTestRule.onNodeWithText("Retry").assertIsDisplayed()
    composeTestRule.onNodeWithText("Retry").performClick()

    assertTrue(retried)
  }
}
