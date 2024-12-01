package eu.slickbot.vremedo.composable

import androidx.compose.foundation.Image
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import eu.slickbot.vremedo.R
import eu.slickbot.vremedo.screen.Screen
import eu.slickbot.vremedo.utils.AppNavigation
import kotlinx.coroutines.launch
import org.koin.compose.getKoin

@Composable
fun AppDrawer(
  state: DrawerState,
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
      ModalDrawerSheet(
        modifier = Modifier.padding(paddingValues),
      ) {
        Spacer(Modifier.height(12.dp))
        Image(
          modifier = modifier.fillMaxWidth(),
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
//        HorizontalDivider()
        Spacer(Modifier.height(12.dp))
        NavigationDrawerItem(
          modifier = Modifier.padding(horizontal = 8.dp),
          label = { Text(text = "Weather") },
          selected = currentScreen == Screen.Weather,
          onClick = {
            appNavigation.navigateToWeather()
            scope.launch { state.close() }
          },
        )
        NavigationDrawerItem(
          modifier = Modifier.padding(horizontal = 8.dp),
          label = { Text(text = "Images") },
          selected = currentScreen == Screen.Images,
          onClick = {
            appNavigation.navigateToImages()
            scope.launch { state.close() }
          },
        )
      }
    },
    content = content,
  )
}
