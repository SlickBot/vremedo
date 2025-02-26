package eu.slickbot.vremedo.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.slickbot.vremedo.R
import eu.slickbot.vremedo.screen.Screen
import eu.slickbot.vremedo.theme.VremedoTheme
import eu.slickbot.vremedo.utils.AppNavigation
import kotlinx.coroutines.launch
import org.koin.compose.getKoin

@Composable
fun AppDrawer(
  state: DrawerState,
  onImageClick: () -> Unit,
  modifier: Modifier = Modifier,
  paddingValues: PaddingValues = PaddingValues(),
  content: @Composable () -> Unit,
) {
  val scope = rememberCoroutineScope()

  val appNavigation by getKoin().inject<AppNavigation>()
  val currentScreen by appNavigation.screen.collectAsState()

  ModalNavigationDrawer(
    modifier = modifier,
    drawerState = state,
    drawerContent = {
      DrawerContent(
        currentScreen = currentScreen,
        paddingValues = paddingValues,
        onImageClick = onImageClick,
        onWeatherItemClick = {
          appNavigation.navigateToWeather()
          scope.launch { state.close() }
        },
        onImagesItemClick = {
          appNavigation.navigateToImages()
          scope.launch { state.close() }
        }
      )
    },
    content = content,
  )
}

@Composable
private fun DrawerContent(
  currentScreen: Screen,
  onImageClick: () -> Unit,
  paddingValues: PaddingValues,
  onWeatherItemClick: () -> Unit,
  onImagesItemClick: () -> Unit,
) {
  ModalDrawerSheet(
    modifier = Modifier.padding(paddingValues),
  ) {
    Spacer(Modifier.height(12.dp))
    @OptIn(ExperimentalFoundationApi::class)
    Image(
      modifier = Modifier
        .fillMaxWidth()
        .combinedClickable(
          interactionSource = remember { MutableInteractionSource() },
          indication = null,
          onClick = {},
          onLongClick = onImageClick,
        ),
      painter = painterResource(R.drawable.edo),
      contentDescription = "Edo",
    )
    Text(
      modifier = Modifier.padding(
        bottom = 16.dp,
        start = 16.dp,
        end = 16.dp,
      ),
      text = "Vremedo",
      style = MaterialTheme.typography.displayLarge,
    )
    Spacer(Modifier.height(12.dp))
    NavigationDrawerItem(
      modifier = Modifier.padding(horizontal = 8.dp),
      label = { Text(text = "Weather") },
      selected = currentScreen == Screen.Weather,
      onClick = onWeatherItemClick,
    )
    NavigationDrawerItem(
      modifier = Modifier.padding(horizontal = 8.dp),
      label = { Text(text = "Images") },
      selected = currentScreen == Screen.Images,
      onClick = onImagesItemClick,
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun AppDrawerPreview() {
  VremedoTheme {
    DrawerContent(
      currentScreen = Screen.Weather,
      paddingValues = PaddingValues(),
      onImageClick = {},
      onWeatherItemClick = {},
      onImagesItemClick = {},
    )
  }
}
