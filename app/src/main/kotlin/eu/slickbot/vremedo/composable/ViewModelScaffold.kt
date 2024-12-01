package eu.slickbot.vremedo.composable

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import eu.slickbot.vremedo.utils.ComponentViewModel

@Composable
fun <T : ComponentViewModel> ViewModelScaffold(
  viewModel: T,
  content: @Composable (PaddingValues) -> Unit,
) {
  DisposableEffect(Unit) {
    /* create */ viewModel.onComposableCreate()
    onDispose { viewModel.onComposableDispose() }
  }
  BaseScaffold(
    content = { paddingValues -> content(paddingValues) },
  )
}
