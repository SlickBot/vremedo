package eu.slickbot.vremedo.extension

import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize

fun ImageBitmap.createPainter(
  srcOffset: IntOffset = IntOffset.Zero,
  srcSize: IntSize = IntSize(width, height),
  filterQuality: FilterQuality = FilterQuality.Low
): BitmapPainter {
  return BitmapPainter(this, srcOffset, srcSize, filterQuality)
}

val ImageBitmap.size: IntSize
  get() = IntSize(width, height)
