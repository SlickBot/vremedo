package eu.slickbot.vremedo.extension

import android.content.Context
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.size.Size
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun ImageLoader.getImageBitmap(context: Context, data: Any?): ImageBitmap? {
  return withContext(Dispatchers.Default) {
    val request = ImageRequest.Builder(context)
      .data(data)
      .size(Size.ORIGINAL)
      .build()

    when (val result = execute(request)) {
      is SuccessResult -> result.drawable.toBitmap().asImageBitmap()
      is ErrorResult -> {
        Log.e("ImageLoader", "Failed to load image: $data", result.throwable)
        null
      }
    }
  }
}
