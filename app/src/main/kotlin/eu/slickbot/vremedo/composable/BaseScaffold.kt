package eu.slickbot.vremedo.composable

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import eu.slickbot.vremedo.utils.ComponentViewModel

@Composable
fun <T : ComponentViewModel> BaseScaffold(
  viewModel: T,
  content: @Composable (PaddingValues) -> Unit,
) {
  DisposableEffect(Unit) {
    /* create */ viewModel.onComposableCreate()
    onDispose { viewModel.onComposableDispose() }
  }
  Scaffold(
    modifier = Modifier.fillMaxSize(),
    containerColor = Color.Transparent,
    content = { paddingValues -> content(paddingValues) },
  )
}
