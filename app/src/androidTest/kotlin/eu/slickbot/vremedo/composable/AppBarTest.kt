package eu.slickbot.vremedo.composable

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import eu.slickbot.vremedo.theme.VremedoTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class AppBarTest {

  @get:Rule
  val composeTestRule = createComposeRule()

  @Test
  fun showsTitle() {
    composeTestRule.setContent {
      VremedoTheme {
        AppBar(title = "Vremedo", onMenuClick = {})
      }
    }

    composeTestRule.onNodeWithText("Vremedo").assertIsDisplayed()
  }

  @Test
  fun showsMenuIcon() {
    composeTestRule.setContent {
      VremedoTheme {
        AppBar(title = "Vremedo", onMenuClick = {})
      }
    }

    composeTestRule.onNodeWithContentDescription("menu").assertIsDisplayed()
  }

  @Test
  fun menuIconInvokesCallback() {
    var clicked = false
    composeTestRule.setContent {
      VremedoTheme {
        AppBar(title = "Vremedo", onMenuClick = { clicked = true })
      }
    }

    composeTestRule.onNodeWithContentDescription("menu").performClick()

    assertTrue(clicked)
  }
}
