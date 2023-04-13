package eu.slickbot.vremedo.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.NonRestartableComposable
import eu.slickbot.vremedo.utils.ComponentViewModel

@Composable
@NonRestartableComposable
fun ViewModelEffect(
    viewModel: ComponentViewModel
) {
    DisposableEffect(Unit) {
        /* create */ viewModel.onScreenCreate()
        onDispose { viewModel.onScreenDispose() }
    }
}
