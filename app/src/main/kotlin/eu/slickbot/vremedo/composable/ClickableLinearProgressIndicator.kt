package eu.slickbot.vremedo.composable

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculateCentroid
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import kotlin.math.roundToInt

@Composable
fun ClickableLinearProgressIndicator(
  currentValue: Int,
  maxValue: Int,
  onValueChange: (Int) -> Unit,
  modifier: Modifier = Modifier
) {
  var _size by remember { mutableStateOf(IntSize.Zero) }
  var _currentValue by remember { mutableIntStateOf(currentValue) }
  var _maxValue by remember { mutableIntStateOf(maxValue) }
  var _onValueChange by remember { mutableStateOf(onValueChange) }

  _currentValue = currentValue
  _maxValue = maxValue
  _onValueChange = onValueChange

  fun onTouchEventLocation(offset: Offset) {
    val factor = offset.x / _size.width
    var value = (_maxValue * factor).roundToInt()
    if (value < 0) value = 0
    if (value > _maxValue) value = _maxValue
    _onValueChange(value)
  }

  LinearProgressIndicator(
    progress = { if (_currentValue == 0) 0f else _currentValue.toFloat() / _maxValue },
    modifier = modifier
      .onSizeChanged { _size = it }
      .pointerInput(Unit) {
        awaitEachGesture {
          val downEvent = awaitFirstDown(requireUnconsumed = false)
          onTouchEventLocation(downEvent.position)

          do {
            val event = awaitPointerEvent()
            val canceled = event.changes.any { it.isConsumed }
            if (canceled) continue

            val centroid = event.calculateCentroid(useCurrent = false)
            if (centroid == Offset.Unspecified) continue

            onTouchEventLocation(centroid)

          } while (!canceled && event.changes.any { it.pressed })
        }
      }
  )
}
