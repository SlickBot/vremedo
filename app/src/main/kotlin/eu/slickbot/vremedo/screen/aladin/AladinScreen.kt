package eu.slickbot.vremedo.screen.aladin

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.slickbot.vremedo.composable.AppScaffold
import eu.slickbot.vremedo.composable.ImageScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun AladinScreen(vm: AladinViewModel = koinViewModel()) {
  val state by vm.state.collectAsStateWithLifecycle()

  AppScaffold { innerPadding ->
    ImageScreen(
      modifier = Modifier.fillMaxSize(),
      innerPadding = innerPadding,
      imageUrls = state.imageUrls,
      isLoading = state.isLoading,
    )
  }
}
