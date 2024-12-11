package eu.slickbot.vremedo.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import eu.slickbot.vremedo.R

@Composable
fun ZoomableImage(
  painter: Painter,
  contentDescription: String?,
  modifier: Modifier = Modifier,
  minScale: Float = 0.1f,
  maxScale: Float = 5f,
  disableRotation: Boolean = false,
) {
  ZoomableBox(
    modifier = modifier,
    minScale = minScale,
    maxScale = maxScale,
    disableRotation = disableRotation,
  ) {
    Image(
      painter = painter,
      contentDescription = contentDescription,
      modifier = Modifier
        .fillMaxSize()
        .graphicsLayer(
          scaleX = scale,
          scaleY = scale,
          translationX = offsetX,
          translationY = offsetY,
          rotationZ = if (disableRotation) 0f else rotation,
        ),
    )
  }
}

@Preview
@Composable
private fun ZoomableImagePreview() {
  ZoomableImage(
    painter = painterResource(R.drawable.edo),
    contentDescription = null,
  )
}
