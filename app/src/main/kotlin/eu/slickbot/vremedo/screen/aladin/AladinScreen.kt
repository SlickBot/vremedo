package eu.slickbot.vremedo.screen.aladin

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Radar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.slickbot.arso.model.ArsoAladinMode
import eu.slickbot.arso.model.ArsoAladinScope
import eu.slickbot.vremedo.composable.AppListDialog
import eu.slickbot.vremedo.composable.AppScaffold
import eu.slickbot.vremedo.composable.ImageScreen
import eu.slickbot.vremedo.composable.ImageScreenButton
import org.koin.androidx.compose.koinViewModel

@Composable
fun AladinScreen(vm: AladinViewModel = koinViewModel()) {
  val state by vm.state.collectAsStateWithLifecycle()

  var showScopesDialog by rememberSaveable { mutableStateOf(false) }
  var showModesDialog by rememberSaveable { mutableStateOf(false) }

  AppScaffold { innerPadding ->
    ImageScreen(
      modifier = Modifier.fillMaxSize(),
      innerPadding = innerPadding,
      title = "Aladin",
      imageUrls = state.imageUrls,
      isLoading = state.isLoading,
      buttonLeft = ImageScreenButton(
        text = "Scope",
        icon = Icons.Filled.Map,
        onClick = { showScopesDialog = true },

        dialog = {
          AppListDialog(
            items = ArsoAladinScope.entries,
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
        icon = Icons.Filled.Radar,
        onClick = { showModesDialog = true },
        dialog = {
          AppListDialog(
            items = ArsoAladinMode.entries,
            itemText = { it.displayName },
            itemSelected = { it == state.mode },
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
