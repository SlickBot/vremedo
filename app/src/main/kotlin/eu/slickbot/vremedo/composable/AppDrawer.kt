package eu.slickbot.vremedo.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    Column(
      modifier = Modifier
        .weight(1f)
        .verticalScroll(rememberScrollState()),
    ) {
      Spacer(Modifier.height(12.dp))
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
    DataSourcesFooter(
      modifier = Modifier.padding(16.dp),
    )
  }
}

@Composable
private fun DataSourcesFooter(
  modifier: Modifier = Modifier,
) {
  val uriHandler = LocalUriHandler.current

  fun open(url: String) {
    runCatching { uriHandler.openUri(url) }
      .onFailure { Timber.w(it, "Failed to open data source link: $url") }
  }

  Column(modifier = modifier) {
    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
    Text(
      modifier = Modifier.padding(top = 16.dp, bottom = 4.dp),
      text = "DATA SOURCES",
      style = MaterialTheme.typography.labelSmall,
      color = MaterialTheme.colorScheme.primary,
      letterSpacing = 1.sp,
    )
    DataSourceRow(
      name = "ARSO",
      description = "Slovenian Environmental Agency",
      onClick = { open(ARSO_URL) },
    )
    DataSourceRow(
      name = "pro-vreme",
      description = "Weather forecasts",
      onClick = { open(PRO_VREME_URL) },
    )
    Text(
      modifier = Modifier.padding(top = 12.dp),
      text = "Vremedo only displays publicly available data and is not " +
        "affiliated with these providers.",
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      lineHeight = 16.sp,
    )
  }
}

@Composable
private fun DataSourceRow(
  name: String,
  description: String,
  onClick: () -> Unit,
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .clickable(onClick = onClick)
      .padding(vertical = 10.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = name,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onSurface,
      )
      Text(
        text = description,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }
    Icon(
      imageVector = Icons.AutoMirrored.Filled.OpenInNew,
      contentDescription = "Open $name website",
      tint = MaterialTheme.colorScheme.onSurfaceVariant,
      modifier = Modifier.size(18.dp),
    )
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
      onImagesItemClick = {},
    )
  }
}
