package eu.slickbot.vremedo.screen.cameras

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.slickbot.arso.model.ArsoCameraLength
import eu.slickbot.arso.model.ArsoCameraOrientation
import eu.slickbot.vremedo.composable.AppScaffold
import eu.slickbot.vremedo.composable.ImageScreen
import eu.slickbot.vremedo.composable.ImageScreenButton
import eu.slickbot.vremedo.composable.SimpleListDialog
import org.koin.androidx.compose.koinViewModel

@Composable
fun CamerasScreen(vm: CamerasViewModel = koinViewModel()) {
  val state by vm.state.collectAsStateWithLifecycle()

  var showOrientationDialog by rememberSaveable { mutableStateOf(false) }
  var showLengthsDialog by rememberSaveable { mutableStateOf(false) }

  AppScaffold { innerPadding ->
    ImageScreen(
      modifier = Modifier.fillMaxSize(),
      innerPadding = innerPadding,
      imageUrls = state.imageUrls,
      isLoading = state.isLoading,
      buttonLeft = ImageScreenButton(
        text = "Scope",
        icon = Icons.Filled.Map,
        onClick = { showOrientationDialog = true },
        dialog = {
          SimpleListDialog(
            items = ArsoCameraOrientation.entries,
            itemText = { it.name },
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
        text = "Mode",
        icon = Icons.Filled.Timelapse,
        onClick = { showLengthsDialog = true },
        dialog = {
          SimpleListDialog(
            items = ArsoCameraLength.entries,
            itemText = { it.name },
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
      )
    )
  }

}
