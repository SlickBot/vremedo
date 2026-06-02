package eu.slickbot.vremedo.composable

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import eu.slickbot.vremedo.theme.VremedoTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class AppListDialogTest {

  @get:Rule
  val composeTestRule = createComposeRule()

  private val cities = listOf("Ljubljana", "Maribor", "Koper")

  @Test
  fun hidesItemsWhenNotVisible() {
    composeTestRule.setContent {
      VremedoTheme {
        AppListDialog(
          items = cities,
          onItemClick = {},
          onDismissRequest = {},
          isVisible = false,
        )
      }
    }

    composeTestRule.onNodeWithText("Ljubljana").assertDoesNotExist()
  }

  @Test
  fun showsAllItemsWhenVisible() {
    composeTestRule.setContent {
      VremedoTheme {
        AppListDialog(
          items = cities,
          onItemClick = {},
          onDismissRequest = {},
          isVisible = true,
        )
      }
    }

    cities.forEach { city ->
      composeTestRule.onNodeWithText(city).assertIsDisplayed()
    }
  }

  @Test
  fun itemClickInvokesCallbackWithItem() {
    var clicked: String? = null
    composeTestRule.setContent {
      VremedoTheme {
        AppListDialog(
          items = cities,
          onItemClick = { clicked = it },
          onDismissRequest = {},
          isVisible = true,
        )
      }
    }

    composeTestRule.onNodeWithText("Maribor").performClick()

    assertEquals("Maribor", clicked)
  }
}
