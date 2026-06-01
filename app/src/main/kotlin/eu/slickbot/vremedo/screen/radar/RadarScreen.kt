package eu.slickbot.vremedo.screen.radar

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
import eu.slickbot.arso.model.ArsoRadarLength
import eu.slickbot.arso.model.ArsoRadarScope
import eu.slickbot.vremedo.composable.AppListDialog
import eu.slickbot.vremedo.composable.AppScaffold
import eu.slickbot.vremedo.composable.ImageScreen
import eu.slickbot.vremedo.composable.ImageScreenButton
import org.koin.androidx.compose.koinViewModel

@Composable
fun RadarScreen(vm: RadarViewModel = koinViewModel()) {
  val state by vm.state.collectAsStateWithLifecycle()

  var showScopesDialog by rememberSaveable { mutableStateOf(false) }
  var showLengthsDialog by rememberSaveable { mutableStateOf(false) }

  AppScaffold { innerPadding ->
    ImageScreen(
      modifier = Modifier.fillMaxSize(),
      innerPadding = innerPadding,
      title = "Radar",
      imageUrls = state.imageUrls,
      isLoading = state.isLoading,
      buttonLeft = ImageScreenButton(
        text = "Scope",
        icon = Icons.Filled.Map,
        onClick = { showScopesDialog = true },
        dialog = {
          AppListDialog(
            items = ArsoRadarScope.entries,
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
        onClick = { showLengthsDialog = true },
        dialog = {
          AppListDialog(
            items = ArsoRadarLength.entries,
            itemText = { it.displayName },
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
      )
    )
  }

}
