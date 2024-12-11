package eu.slickbot.vremedo.screen.images

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import eu.slickbot.vremedo.composable.AppBar
import eu.slickbot.vremedo.composable.AppDrawer
import eu.slickbot.vremedo.composable.AppScaffold
import eu.slickbot.vremedo.theme.VremedoTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun ImagesScreen(vm: ImagesViewModel = koinViewModel()) {
  AppScaffold { paddingValues ->
    Content(
      paddingValues = paddingValues,
      onAladinClick = vm::onAladinClick,
      onRadarClick = vm::onRadarClick,
      onSatelliteClick = vm::onSatelliteClick,
      onCamerasClick = vm::onCamerasClick,
    )
  }
}

@Composable
private fun Content(
  paddingValues: PaddingValues,
  onAladinClick: () -> Unit,
  onRadarClick: () -> Unit,
  onSatelliteClick: () -> Unit,
  onCamerasClick: () -> Unit,
) {
  val scope = rememberCoroutineScope()
  val drawerState = rememberDrawerState(DrawerValue.Closed)

  fun openMenu() {
    scope.launch { drawerState.open() }
  }

  AppDrawer(drawerState) {
    Column(
      modifier = Modifier.padding(paddingValues),
    ) {
      AppBar(
        modifier = Modifier.fillMaxWidth(),
        title = "Images",
        onMenuClick = ::openMenu,
      )
      Spacer(Modifier.height(20.dp))
      ImageItem(
        modifier = Modifier,
        text = "Aladin",
        imageUrl = "https://meteo.arso.gov.si/uploads/probase/www/plus/thumb/as_tcc-rr_si-neighbours_thumb.png",
        onClick = onAladinClick,
      )
      ImageItem(
        modifier = Modifier,
        text = "Radar",
        imageUrl = "https://meteo.arso.gov.si/uploads/probase/www/plus/thumb/si0_zm_si_thumb.jpg",
        onClick = onRadarClick,
      )
      ImageItem(
        modifier = Modifier,
        text = "Satellite",
        imageUrl = "https://meteo.arso.gov.si/uploads/probase/www/plus/thumb/msg_hrv_si_thumb.jpg",
        onClick = onSatelliteClick,
      )
      ImageItem(
        modifier = Modifier,
        text = "Cameras",
        imageUrl = "https://meteo.arso.gov.si/uploads/probase/www/plus/thumb/siwc_LJUBL-ANA_BEZIGRAD_nw_thumb.jpg",
        onClick = onCamerasClick,
      )
    }
  }
}

@Composable
private fun ImageItem(
  text: String,
  imageUrl: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .clickable { onClick() }
      .padding(horizontal = 16.dp, vertical = 20.dp),
    horizontalArrangement = Arrangement.spacedBy(24.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Image(
      modifier = Modifier
//        .fillMaxSize()
        .width(100.dp)
        .aspectRatio(1.3f / 1f),
      painter = rememberAsyncImagePainter(imageUrl),
      contentDescription = null,
      contentScale = ContentScale.Crop,
    )
    Text(
      text = text,
      style = MaterialTheme.typography.headlineMedium,
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun ImagesScreenPreview() {
  VremedoTheme {
    Content(
      paddingValues = PaddingValues(),
      onAladinClick = {},
      onRadarClick = {},
      onSatelliteClick = {},
      onCamerasClick = {},
    )
  }
}
