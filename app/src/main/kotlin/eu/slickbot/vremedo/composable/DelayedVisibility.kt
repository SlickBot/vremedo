package eu.slickbot.vremedo.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

@Composable
fun rememberDelayed(
  target: Boolean,
  delayMillis: Long = 300L,
): Boolean {
  var current by remember { mutableStateOf(false) }
  LaunchedEffect(target) {
    if (target) {
      delay(delayMillis)
      current = true
    } else {
      current = false
    }
  }
  return current
}
