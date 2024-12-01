package eu.slickbot.vremedo.screen.images

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import eu.slickbot.vremedo.composable.AppBar
import eu.slickbot.vremedo.composable.AppDrawer
import eu.slickbot.vremedo.composable.ViewModelScaffold
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun ImagesScreen(vm: ImagesViewModel = koinViewModel()) {
  val scope = rememberCoroutineScope()
  val drawerState = rememberDrawerState(DrawerValue.Closed)

  fun openMenu() {
    scope.launch { drawerState.open() }
  }

  ViewModelScaffold(vm) { paddingValues ->
    AppDrawer(drawerState) {
      Column(
        modifier = Modifier.padding(paddingValues),
      ) {
        AppBar(
          modifier = Modifier.fillMaxWidth(),
          title = "Images",
          onMenuClick = ::openMenu,
        )
      }
    }
  }
}
