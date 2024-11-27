package eu.slickbot.vremedo.extension

import android.content.Context
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ImageRequest
import coil.size.Size
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun ImageLoader.getImageBitmap(context: Context, data: Any?): ImageBitmap {
    return withContext(Dispatchers.Default) {
        val request = ImageRequest.Builder(context)
            .data(data)
            .size(Size.ORIGINAL)
            .build()

        val drawable = execute(request).drawable!!

        drawable.toBitmap().asImageBitmap()
    }
}
