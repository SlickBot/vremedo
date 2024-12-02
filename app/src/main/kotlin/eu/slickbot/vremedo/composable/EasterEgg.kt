package eu.slickbot.vremedo.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import eu.slickbot.vremedo.R
import kotlinx.coroutines.Job

private const val HIDE_DELAY = 2000L

@Composable
fun EasterEgg(
  show: Boolean,
  onHide: () -> Unit,
) {
  var scale by remember { mutableFloatStateOf(0f) }
  var alpha by remember { mutableFloatStateOf(0f) }
  var rotation by remember { mutableFloatStateOf(0f) }

//  LaunchedEffect(show) {
//    scale = 0f
//    alpha = 0f
//    rotation = 0f
//  }

  val scaleAnimatable = remember { Animatable(scale) }
  val alphaAnimatable = remember { Animatable(alpha) }
  val rotationAnimatable = remember { Animatable(rotation) }
  val coroutineScope = rememberCoroutineScope()

  // Job to manage the auto-hide timer
  var hideJob by remember { mutableStateOf<Job?>(null) }

  // Start opening animation
  LaunchedEffect(show) {
    if (show) {
      scaleAnimatable.snapTo(0f)
      alphaAnimatable.snapTo(0f)
      rotationAnimatable.snapTo(0f)

      coroutineScope.launch {
        scaleAnimatable.animateTo(
          targetValue = 1f,
          animationSpec = tween(durationMillis = 300),
        )
      }
      coroutineScope.launch {
        alphaAnimatable.animateTo(
          targetValue = 1f,
          animationSpec = tween(durationMillis = 300),
        )
      }
      coroutineScope.launch {
        rotationAnimatable.animateTo(
          targetValue = 1f,
          animationSpec = tween(durationMillis = 300),
        )
      }
    }
  }

  // Auto-hide after 2 seconds
  LaunchedEffect(show) {
    if (show) {
      hideJob?.cancel()
      hideJob = coroutineScope.launch {
        delay(HIDE_DELAY)
        onHide()
      }
    }
  }

  AnimatedVisibility(
    visible = show,
    enter = fadeIn(),
    exit = fadeOut(),
  ) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(Color.Black.copy(alpha = 0.8f))
        .clickable(
          interactionSource = remember { MutableInteractionSource() },
          indication = null,
        ) {
          hideJob?.cancel()
          onHide()
        },
      contentAlignment = Alignment.Center,
    ) {
      Image(
        painter = painterResource(R.drawable.edo),
        contentDescription = null,
        modifier = Modifier
          .size(300.dp)
          .scale(scaleAnimatable.value)
          .alpha(alphaAnimatable.value)
          .rotate(rotationAnimatable.value * 360f)
          .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
          ) {
            hideJob?.cancel()
            hideJob = coroutineScope.launch {
              // Play spring animation
              scaleAnimatable.animateTo(
                targetValue = .8f,
                animationSpec = tween(durationMillis = 50),
              )
              scaleAnimatable.animateTo(
                targetValue = 1f,
                animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy),
              )
              delay(HIDE_DELAY)
              onHide()
            }
          }
      )
    }
  }
}
