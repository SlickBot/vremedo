package eu.slickbot.vremedo.extension

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import androidx.core.graphics.drawable.DrawableCompat

fun Context.getBitmapFromVectorDrawable(@DrawableRes drawableId: Int): Bitmap {
  val drawable = DrawableCompat.wrap(ContextCompat.getDrawable(this, drawableId)!!).mutate()
  val bitmap = createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight)
  val canvas = Canvas(bitmap)
  drawable.setBounds(0, 0, canvas.width, canvas.height)
  drawable.draw(canvas)
  return bitmap
}
