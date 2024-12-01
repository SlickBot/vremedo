package eu.slickbot.vremedo.composable

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

@Composable
fun AnimatedImage(
  images: List<Int>,
  idx: Int,
  modifier: Modifier = Modifier,
  contentScale: ContentScale = ContentScale.Crop,
  durationMillis: Int = 350,
  label: String = "AnimatedImage",
) {
  Crossfade(
    targetState = idx,
    animationSpec = tween(durationMillis = durationMillis),
    label = label,
  ) { target ->
    Image(
      modifier = modifier,
      painter = painterResource(images[target]),
      contentDescription = null,
      contentScale = contentScale,
    )
  }
}
