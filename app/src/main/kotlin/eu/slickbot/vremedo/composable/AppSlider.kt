package eu.slickbot.vremedo.composable

import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import eu.slickbot.vremedo.theme.VremedoTheme

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

class SliderBarState {

  var value by mutableFloatStateOf(0f)
  var minValue by mutableFloatStateOf(0f)
  var maxValue by mutableFloatStateOf(0f)

  companion object {
    val Saver: Saver<SliderBarState, *> = listSaver(
      save = { listOf(it.value, it.minValue, it.maxValue) },
      restore = { saved ->
        SliderBarState().also {
          it.value = saved[0]
          it.minValue = saved[1]
          it.maxValue = saved[2]
        }
      },
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
