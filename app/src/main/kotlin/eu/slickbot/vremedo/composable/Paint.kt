package eu.slickbot.vremedo.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import android.graphics.Paint as AndroidPaint
import androidx.compose.ui.graphics.Paint as ComposePaint

@Composable
fun rememberAndroidPaint(builder: AndroidPaint.() -> Unit): AndroidPaint {
  return remember { AndroidPaint().apply(builder) }
}

@Composable
fun rememberPaint(builder: ComposePaint.() -> Unit): ComposePaint {
  return remember { ComposePaint().apply(builder) }
}
