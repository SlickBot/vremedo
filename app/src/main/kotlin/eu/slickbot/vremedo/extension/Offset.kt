package eu.slickbot.vremedo.extension

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.util.fastRoundToInt

fun Offset.toIntOffset(): IntOffset {
  return IntOffset(x.toInt(), y.toInt())
}

fun Offset.roundToIntOffset(): IntOffset {
  return IntOffset(x.fastRoundToInt(), y.fastRoundToInt())
}
