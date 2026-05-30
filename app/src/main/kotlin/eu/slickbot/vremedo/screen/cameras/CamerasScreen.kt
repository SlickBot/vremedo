package eu.slickbot.vremedo.screen.cameras

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.slickbot.arso.model.ArsoCameraLength
import eu.slickbot.vremedo.composable.AppListDialog
import eu.slickbot.vremedo.composable.AppScaffold
import eu.slickbot.vremedo.composable.ImageScreen
import eu.slickbot.vremedo.composable.ImageScreenButton
import eu.slickbot.vremedo.theme.App
import eu.slickbot.vremedo.theme.VremedoTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun CamerasScreen(vm: CamerasViewModel = koinViewModel()) {
  val state by vm.state.collectAsStateWithLifecycle()

  var showOrientationDialog by rememberSaveable { mutableStateOf(false) }
  var showLengthsDialog by rememberSaveable { mutableStateOf(false) }
  var showCitiesDialog by rememberSaveable { mutableStateOf(false) }

  AppScaffold { innerPadding ->
    ImageScreen(
      modifier = Modifier.fillMaxSize(),
      innerPadding = innerPadding,
      title = "Cameras",
      imageUrls = state.imageUrls,
      isLoading = state.isLoading,
      buttonLeft = ImageScreenButton(
        text = "Orientation",
        icon = Icons.App.Compass,
        onClick = { showOrientationDialog = true },
        dialog = {
          AppListDialog(
            items = state.selectedCameraData?.orientations.orEmpty(),
            itemText = { it.name },
            itemSelected = { it == state.orientation },
            onItemClick = {
              vm.setOrientation(it)
              showOrientationDialog = false
            },
            onDismissRequest = {
              showOrientationDialog = false
            },
            isVisible = showOrientationDialog,
          )
        }
      ),
      buttonRight = ImageScreenButton(
        text = "Length",
        icon = Icons.Filled.Timer,
        onClick = { showLengthsDialog = true },
        dialog = {
          AppListDialog(
            items = ArsoCameraLength.entries,
            itemText = { it.name },
            itemSelected = { it == state.length },
            onItemClick = {
              vm.setLength(it)
              showLengthsDialog = false
            },
            onDismissRequest = {
              showLengthsDialog = false
            },
            isVisible = showLengthsDialog,
          )
        }
      ),
      extraContent = {
        VremedoTheme(darkTheme = true) {
          Text(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { showCitiesDialog = !showCitiesDialog }
              .padding(horizontal = 16.dp, vertical = 8.dp),
            text = state.selectedCameraData?.title.orEmpty(),
            fontSize = 26.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
          )
        }
      },
    )
  }

  AppListDialog(
    items = state.cameraData,
    itemText = { it.title },
    itemSelected = { it == state.selectedCameraData },
    onItemClick = {
      vm.setCameraData(it)
      showCitiesDialog = false
    },
    onDismissRequest = {
      showCitiesDialog = false
    },
    isVisible = showCitiesDialog,
  )

}
