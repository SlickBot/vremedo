package eu.slickbot.vremedo.composable

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import eu.slickbot.vremedo.R

@Composable
fun BackgroundBox(
  isNight: Boolean = true,
  content: @Composable BoxScope.() -> Unit,
) {
  val images = remember {
    listOf(
      R.drawable.background_dark,
      R.drawable.background_light,
    )
  }

  Surface(Modifier.fillMaxSize()) {
    if (images.isNotEmpty()) {
      AnimatedImage(
        modifier = Modifier.fillMaxSize(),
        images = images,
        idx = if (isNight) 0 else 1,
        durationMillis = 1000,
      )
    }
    FullSizeBox(content = content)
  }
}
