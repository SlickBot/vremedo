package eu.slickbot.vremedo.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.Radar
import androidx.compose.material.icons.outlined.SatelliteAlt
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.slickbot.vremedo.BuildConfig
import eu.slickbot.vremedo.R
import eu.slickbot.vremedo.screen.Screen
import eu.slickbot.vremedo.theme.VremedoTheme
import eu.slickbot.vremedo.utils.AppNavigation
import kotlinx.coroutines.launch
import org.koin.compose.getKoin
import timber.log.Timber

private const val ARSO_URL = "https://meteo.arso.gov.si"
private const val PRO_VREME_URL = "https://www.pro-vreme.net"

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

  fun navigateAndClose(navigate: () -> Unit) {
    navigate()
    scope.launch { state.close() }
  }

  ModalNavigationDrawer(
    modifier = modifier,
    drawerState = state,
    drawerContent = {
      DrawerContent(
        currentScreen = currentScreen,
        paddingValues = paddingValues,
        onImageClick = onImageClick,
        onWeatherItemClick = { navigateAndClose(appNavigation::navigateToWeather) },
        onAladinItemClick = { navigateAndClose(appNavigation::navigateToAladin) },
        onRadarItemClick = { navigateAndClose(appNavigation::navigateToRadar) },
        onSatelliteItemClick = { navigateAndClose(appNavigation::navigateToSatellite) },
        onCamerasItemClick = { navigateAndClose(appNavigation::navigateToCameras) },
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
  onAladinItemClick: () -> Unit,
  onRadarItemClick: () -> Unit,
  onSatelliteItemClick: () -> Unit,
  onCamerasItemClick: () -> Unit,
) {
  DrawerScaffold(
    paddingValues = paddingValues,
    header = {
      Spacer(Modifier.height(24.dp))
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
        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp, start = 16.dp, end = 16.dp),
        text = "Vremedo",
        style = MaterialTheme.typography.headlineLarge,
      )
    },
    footer = { DataSourcesFooter() },
  ) {
    DrawerSectionLabel(text = "FORECAST")
    DrawerScreenItem(
      icon = Icons.Outlined.WbSunny,
      label = "Weather",
      selected = currentScreen == Screen.Weather,
      onClick = onWeatherItemClick,
    )
    DrawerSectionLabel(text = "IMAGERY")
    DrawerScreenItem(
      icon = Icons.Outlined.Map,
      label = "Aladin",
      selected = currentScreen == Screen.Aladin,
      onClick = onAladinItemClick,
    )
    DrawerScreenItem(
      icon = Icons.Outlined.Radar,
      label = "Radar",
      selected = currentScreen == Screen.Radar,
      onClick = onRadarItemClick,
    )
    DrawerScreenItem(
      icon = Icons.Outlined.SatelliteAlt,
      label = "Satellite",
      selected = currentScreen == Screen.Satellite,
      onClick = onSatelliteItemClick,
    )
    DrawerScreenItem(
      icon = Icons.Outlined.PhotoCamera,
      label = "Cameras",
      selected = currentScreen == Screen.Cameras,
      onClick = onCamerasItemClick,
    )
  }
}

@Composable
private fun DataSourcesFooter() {
  val uriHandler = LocalUriHandler.current

  fun open(url: String) {
    runCatching { uriHandler.openUri(url) }
      .onFailure { Timber.w(it, "Failed to open data source link: $url") }
  }

  Column(modifier = Modifier.padding(bottom = 8.dp)) {
    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
    DrawerSectionLabel(text = "DATA SOURCES")
    DrawerSourceItem(
      name = "ARSO",
      description = "Slovenian Environmental Agency",
      onClick = { open(ARSO_URL) },
    )
    DrawerSourceItem(
      name = "pro-vreme",
      description = "Weather forecasts",
      onClick = { open(PRO_VREME_URL) },
    )
    DrawerTextItem(text = "Version ${BuildConfig.VERSION_NAME}")
  }
}

@Preview(showBackground = true, heightDp = 720)
@Composable
private fun AppDrawerPreview() {
  VremedoTheme {
    DrawerContent(
      currentScreen = Screen.Weather,
      paddingValues = PaddingValues(),
      onImageClick = {},
      onWeatherItemClick = {},
      onAladinItemClick = {},
      onRadarItemClick = {},
      onSatelliteItemClick = {},
      onCamerasItemClick = {},
    )
  }
}
