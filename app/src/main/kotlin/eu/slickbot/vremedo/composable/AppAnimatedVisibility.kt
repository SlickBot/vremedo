package eu.slickbot.vremedo.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AppAnimatedVisibility(
  visible: Boolean,
  modifier: Modifier = Modifier,
  enter: EnterTransition = fadeIn(),
  exit: ExitTransition = fadeOut(),
  content: @Composable() AnimatedVisibilityScope.() -> Unit,
) {
  AnimatedVisibility(
    modifier = modifier,
    visible = visible,
    enter = enter,
    exit = exit,
    content = content,
  )
}
