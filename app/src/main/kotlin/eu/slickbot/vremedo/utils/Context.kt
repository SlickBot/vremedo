package eu.slickbot.vremedo.utils

import android.content.Context
import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

fun Context.getBitmapFromVectorDrawable(@DrawableRes drawableId: Int): Bitmap {
  val drawable = DrawableCompat.wrap(ContextCompat.getDrawable(this, drawableId)!!).mutate()
  val bitmap = Bitmap.createBitmap(
    drawable.intrinsicWidth,
    drawable.intrinsicHeight,
    Bitmap.Config.ARGB_8888,
  )
  val canvas = android.graphics.Canvas(bitmap)
  drawable.setBounds(0, 0, canvas.width, canvas.height)
  drawable.draw(canvas)
  return bitmap
}
