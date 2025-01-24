package eu.slickbot.vremedo.screen.satellite

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
import eu.slickbot.arso.model.ArsoSatelliteLength
import eu.slickbot.arso.model.ArsoSatelliteScope
import eu.slickbot.vremedo.composable.AppScaffold
import eu.slickbot.vremedo.composable.ImageScreen
import eu.slickbot.vremedo.composable.ImageScreenButton
import eu.slickbot.vremedo.composable.SimpleListDialog
import org.koin.androidx.compose.koinViewModel

@Composable
fun SatelliteScreen(vm: SatelliteViewModel = koinViewModel()) {
  val state by vm.state.collectAsStateWithLifecycle()

  var showScopesDialog by rememberSaveable { mutableStateOf(false) }
  var showModesDialog by rememberSaveable { mutableStateOf(false) }

  AppScaffold { innerPadding ->
    ImageScreen(
      modifier = Modifier.fillMaxSize(),
      innerPadding = innerPadding,
      imageUrls = state.imageUrls,
      isLoading = state.isLoading,
      buttonLeft = ImageScreenButton(
        text = "Scope",
        icon = Icons.Filled.Map,
        onClick = { showScopesDialog = true },
        dialog = {
          SimpleListDialog(
            items = ArsoSatelliteScope.entries,
            itemText = { it.name },
            onItemClick = {
              vm.setScope(it)
              showScopesDialog = false
            },
            onDismissRequest = {
              showScopesDialog = false
            },
            isVisible = showScopesDialog,
          )
        }
      ),
      buttonRight = ImageScreenButton(
        text = "Mode",
        icon = Icons.Filled.Timelapse,
        onClick = { showModesDialog = true },
        dialog = {
          SimpleListDialog(
            items = ArsoSatelliteLength.entries,
            itemText = { it.name },
            onItemClick = {
              vm.setMode(it)
              showModesDialog = false
            },
            onDismissRequest = {
              showModesDialog = false
            },
            isVisible = showModesDialog,
          )
        }
      )
    )
  }
}
