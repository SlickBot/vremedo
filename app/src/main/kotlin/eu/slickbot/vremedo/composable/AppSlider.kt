package eu.slickbot.vremedo.composable

import android.os.Parcelable
import androidx.compose.material3.Slider
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import eu.slickbot.vremedo.theme.VremedoTheme
import kotlinx.parcelize.Parcelize

@Composable
fun AppSlider(
  state: SliderBarState,
  modifier: Modifier = Modifier,
) {
  fun getSliderValue(): Float {
    return (state.value - state.minValue) / (state.maxValue - state.minValue)
  }

  var value by remember { mutableFloatStateOf(getSliderValue()) }

  LaunchedEffect(state.value) {
    value = getSliderValue()
  }

  Slider(
    value = value,
    onValueChange = { state.value = ((state.maxValue - state.minValue) * it) + state.minValue },
    modifier = modifier
  )
}

@Parcelize
class AppSliderStateSavable(
  private val value: Float,
  private val minValue: Float,
  private val maxValue: Float,
) : Parcelable {

  constructor(savable: SliderBarState) : this(
    savable.value,
    savable.minValue,
    savable.maxValue,
  )

  fun toState() = SliderBarState().also {
    it.value = value
    it.minValue = minValue
    it.maxValue = maxValue
  }
}

class SliderBarState {

  var value by mutableFloatStateOf(0f)
  var minValue by mutableFloatStateOf(0f)
  var maxValue by mutableFloatStateOf(0f)

  companion object {
    val Saver: Saver<SliderBarState, AppSliderStateSavable> = Saver(
      save = { AppSliderStateSavable(it) },
      restore = { it.toState() },
    )
  }
}

@Composable
inline fun rememberAppSliderState(
  key: String? = null,
  crossinline init: SliderBarState.() -> Unit = {}
): SliderBarState = rememberSaveable(key, saver = SliderBarState.Saver) {
  SliderBarState().apply(init)
}

@Preview(showBackground = true)
@Composable
private fun AppSliderPreview() {
  val state = rememberAppSliderState("AppSlider") {
    minValue = 0f
    maxValue = 1f
    value = .7f
  }

  VremedoTheme {
    AppSlider(state)
  }
}
