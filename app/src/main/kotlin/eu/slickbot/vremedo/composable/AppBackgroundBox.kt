package eu.slickbot.vremedo.composable

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import eu.slickbot.vremedo.R

@Composable
fun AppBackgroundBox(
  isNight: Boolean? = true,
  content: @Composable BoxScope.() -> Unit,
) {
  val images = remember {
    listOf(
      R.drawable.background_dark,
      R.drawable.background_light,
    )
  }

  if (isNight != null) {
    Surface(
      modifier = Modifier
        .fillMaxSize()
        .background(colorResource(R.color.app_primary_color)),
    ) {
      if (images.isNotEmpty()) {
        Crossfade(
          targetState = if (isNight) 0 else 1,
          animationSpec = tween(durationMillis = 350),
          label = "AnimatedImage",
        ) { target ->
          Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(images[target]),
            contentDescription = null,
            contentScale = ContentScale.Crop,
          )
        }
      }
      Box(
        modifier = Modifier.fillMaxSize(),
        content = content,
      )
    }
  }

}
