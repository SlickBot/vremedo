package eu.slickbot.vremedo.extension

import android.graphics.Paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size

fun Paint.getTextBounds(
    text: String,
): Rect {
    val bounds = android.graphics.Rect()
    getTextBounds(text, 0, text.length, bounds)
    val offset = Offset(bounds.left.toFloat(), bounds.top.toFloat())
    val size = Size(bounds.width().toFloat(), bounds.height().toFloat())
    return Rect(offset, size)
}
