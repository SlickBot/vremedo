package eu.slickbot.vremedo.composable

import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize

@Composable
fun ZoomableBox(
  modifier: Modifier = Modifier,
  minScale: Float = 1f,
  maxScale: Float = 5f,
  disableRotation: Boolean = true,
  content: @Composable ZoomableBoxScope.() -> Unit,
) {
  var size by remember { mutableStateOf(IntSize.Zero) }
  var scale by remember { mutableFloatStateOf(1f) }
  var offsetX by remember { mutableFloatStateOf(0f) }
  var offsetY by remember { mutableFloatStateOf(0f) }
  var rotation by remember { mutableFloatStateOf(0f) }

  Box(
    modifier = modifier
      .fillMaxSize()
      .clip(RectangleShape)
      .onSizeChanged { size = it }
      .pointerInput(Unit) {
        detectTransformGestures { _, pan, zoom, rot ->
          scale = (scale * zoom).coerceIn(minScale, maxScale)
          val maxX = (size.width * (scale - 1)) / 2
          val minX = -maxX
          offsetX = if (minX < maxX) (offsetX + pan.x).coerceIn(minX, maxX) else offsetX
          val maxY = (size.height * (scale - 1)) / 2
          val minY = -maxY
          offsetY = if (minY < maxY) (offsetY + pan.y).coerceIn(minY, maxY) else offsetY
          if (!disableRotation) {
            rotation += rot
          }
        }
      }
  ) {
    val scope = ZoomableBoxScopeImpl(size, scale, offsetX, offsetY, rotation)
    scope.content()
  }
}

interface ZoomableBoxScope {
  val size: IntSize
  val scale: Float
  val offsetX: Float
  val offsetY: Float
  val rotation: Float
}

private data class ZoomableBoxScopeImpl(
  override val size: IntSize,
  override val scale: Float,
  override val offsetX: Float,
  override val offsetY: Float,
  override val rotation: Float,
) : ZoomableBoxScope
