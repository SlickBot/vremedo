package eu.slickbot.vremedo.screen.satellite

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Timer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.slickbot.arso.model.ArsoSatelliteLength
import eu.slickbot.arso.model.ArsoSatelliteScope
import eu.slickbot.vremedo.composable.AppListDialog
import eu.slickbot.vremedo.composable.AppScaffold
import eu.slickbot.vremedo.composable.ImageScreen
import eu.slickbot.vremedo.composable.ImageScreenButton
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
      title = "Satellite",
      imageUrls = state.imageUrls,
      isLoading = state.isLoading,
      isError = state.isError,
      onRetry = vm::retry,
      buttonLeft = ImageScreenButton(
        text = "Scope",
        icon = Icons.Filled.Map,
        onClick = { showScopesDialog = true },
        dialog = {
          AppListDialog(
            items = ArsoSatelliteScope.entries,
            itemText = { it.displayName },
            itemSelected = { it == state.scope },
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
        icon = Icons.Filled.Timer,
        onClick = { showModesDialog = true },
        dialog = {
          AppListDialog(
            items = ArsoSatelliteLength.entries,
            itemText = { it.displayName },
            itemSelected = { it == state.length },
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
