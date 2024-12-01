package eu.slickbot.vremedo.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import eu.slickbot.vremedo.R

@Composable
fun BackgroundBox(
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
        AnimatedImage(
          modifier = Modifier.fillMaxSize(),
          images = images,
          idx = if (isNight) 0 else 1,
          durationMillis = 1000,
        )
      }
      Box(
        modifier = Modifier.fillMaxSize(),
        content = content,
      )
    }
  }

}
