package eu.slickbot.vremedo.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import eu.slickbot.vremedo.extension.runIf
import eu.slickbot.vremedo.utils.ComponentViewModel

@Composable
fun <T : ComponentViewModel> BaseScreen(
    viewModel: T,
    fitsSystemWindows: Boolean,
    content: @Composable T.() -> Unit,
) {
    DisposableEffect(Unit) {
        /* create */ viewModel.onComposableCreate()
        onDispose { viewModel.onComposableDispose() }
    }
    Box(
        modifier = Modifier.fillMaxSize()
            .runIf(fitsSystemWindows) { systemBarsPadding() },
        content = { content(viewModel) },
    )
}
