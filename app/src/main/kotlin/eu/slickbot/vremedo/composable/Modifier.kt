package eu.slickbot.vremedo.composable

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity

@Composable
fun Modifier.keyboardOnlyPadding(): Modifier {
  val density = LocalDensity.current
  val imeBottom = WindowInsets.ime.getBottom(density)
  val navBarBottom = WindowInsets.navigationBars.getBottom(density)

  return padding(
    bottom = with(density) { (imeBottom - navBarBottom).coerceAtLeast(0).toDp() },
  )
}
