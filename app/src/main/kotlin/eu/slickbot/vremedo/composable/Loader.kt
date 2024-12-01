package eu.slickbot.vremedo.composable

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import eu.slickbot.vremedo.R
import eu.slickbot.vremedo.extension.runIf
import eu.slickbot.vremedo.theme.colorDarkOverlay
import eu.slickbot.vremedo.theme.colorLightOverlay

@Composable
fun Loader(
  show: Boolean,
  modifier: Modifier = Modifier,
  showOverlay: Boolean = false,
  isDarkMode: Boolean = false,
  rotationDuration: Int = 1000,
) {
  val transition = rememberInfiniteTransition(
    label = "LoaderTransition",
  )
  val angle = transition.animateFloat(
    initialValue = 0f,
    targetValue = 360f,
    animationSpec = infiniteRepeatable(
      animation = tween(rotationDuration, easing = LinearEasing),
      repeatMode = RepeatMode.Restart,
    ),
    label = "LoaderAngle",
  )

  Box(
    modifier = modifier.fillMaxSize()
  ) {
    AnimatedContent(
      targetState = show,
      label = "LoaderContent",
    ) { shouldShow ->
      if (shouldShow) {
        Box(
          modifier = modifier
            .fillMaxSize()
            .runIf(showOverlay) {
              background(if (isDarkMode) colorDarkOverlay else colorLightOverlay)
            }
        ) {
          Image(
            modifier = modifier
              .size(160.dp)
              .graphicsLayer { rotationZ = angle.value }
              .align(Alignment.Center),
            painter = painterResource(R.drawable.edo),
            contentDescription = null,
          )
        }
      }
    }
  }
}
