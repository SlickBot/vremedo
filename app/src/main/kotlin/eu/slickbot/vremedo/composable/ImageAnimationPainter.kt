package eu.slickbot.vremedo.composable

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.imageLoader
import eu.slickbot.vremedo.extension.createPainter
import eu.slickbot.vremedo.extension.getImageBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun rememberImageAnimationPainter(
  imageUrls: List<String>,
  imageLoader: ImageLoader = LocalContext.current.imageLoader,
  isPlaying: Boolean = true,
  delay: Long = 1000,
  contentScale: ContentScale = ContentScale.Fit,
  filterQuality: FilterQuality = DrawScope.DefaultFilterQuality,
): ImageAnimationPainter {
  val painter = remember(imageUrls) { ImageAnimationPainter(imageUrls, imageLoader) }
  painter.contentScale = contentScale
  painter.filterQuality = filterQuality
  painter.delay = delay
  painter.isPlaying = isPlaying
  painter.context = LocalContext.current
  painter.onRemembered()
  return painter
}

class ImageAnimationPainter(
  val imageUrls: List<String>,
  private val imageLoader: ImageLoader,
) : Painter(), RememberObserver {

  sealed class State {
    object Empty : State()
    data class Loading(val percentage: Float) : State()
    object Ready : State()
    data class Error(val throwable: Throwable) : State()
  }

  internal var delay = 1000L
  internal var isPlaying = true
  internal var isPreview = false
  internal var context: Context? = null
  internal var filterQuality = DrawScope.DefaultFilterQuality
  internal var contentScale = ContentScale.Fit

  private var bitmaps: List<ImageBitmap>? = null

  private var rememberScope: CoroutineScope? = null

  private val drawSize = MutableStateFlow(Size.Zero)
  private var alpha by mutableFloatStateOf(DefaultAlpha)
  private var painter by mutableStateOf<Painter?>(null)

  private var _index by mutableIntStateOf(0)

  var state by mutableStateOf<State>(State.Empty)
    private set

  fun showNext() {
    val bitmaps = bitmaps ?: return

    _index += 1
    if (_index >= bitmaps.size)
      _index = 0

    _painter = bitmaps[_index].createPainter(filterQuality = filterQuality)
  }

  fun showPrevious() {
    val bitmaps = bitmaps ?: return

    if (bitmaps.isEmpty()) {
      _index = 0
    } else {
      _index -= 1
      if (_index < 0) {
        _index = bitmaps.size - 1
      }

      _painter = bitmaps[_index].createPainter(filterQuality = filterQuality)
    }
  }

  fun setIndex(value: Int) {
    if (value == _index) return
    val bitmaps = bitmaps ?: return
    if (bitmaps.isNotEmpty()) {
      _index = value.coerceIn(0, bitmaps.lastIndex)
      _painter = bitmaps[_index].createPainter(filterQuality = filterQuality)
    }
  }

  fun getIndex(): Int {
    return _index
  }

  // These fields allow access to the current value
  // instead of the value in the current composition.
  private var _state: State = State.Empty
    set(value) {
      field = value
      state = value
    }

  private var _painter: Painter? = null
    set(value) {
      field = value
      painter = value
    }

  private var updatingJob: Job? = null

  override val intrinsicSize: Size
    get() = painter?.intrinsicSize ?: Size.Unspecified

  override fun DrawScope.onDraw() {
    // Update the draw scope's current size
    drawSize.value = size

    // Draw the current painter
    painter?.apply { draw(size, alpha) }
  }

  override fun onRemembered() {
    // Short circuit if we're already remembered.
    if (rememberScope != null) return

    // If we're in inspection mode skip the image request and set the state to loading.
    if (isPreview) {
//      val request = request.newBuilder().defaults(imageLoader.defaults).build()
//      updateState(AsyncImagePainter.State.Loading(request.placeholder?.toPainter()))
      return
    }

    // Create a new scope to observe state and execute requests while we're remembered.
    val scope = CoroutineScope(SupervisorJob() + Main.immediate)
    rememberScope = scope

    // Observe the current request and execute any emissions.
    scope.launch(Default) {
      val context = context
        ?: return@launch

      if (imageUrls.isEmpty())
        return@launch

      withContext(Main) {
        state = State.Loading(0f)
      }

      // initialize
      var done = 0
      var throwable: Throwable? = null
      val values = MutableList<ImageBitmap?>(imageUrls.size) { null }
      imageUrls.mapIndexed { i, url ->
        scope.launch(IO) {
          runCatching {
            values[i] = imageLoader.getImageBitmap(context, url)
          }.fold(
            onSuccess = {
              withContext(Main) {
                done += 1
                _state = State.Loading(done / imageUrls.lastIndex.toFloat())
              }
            },
            onFailure = { throwable = it },
          )
        }
      }.joinAll()

      if (throwable != null) {
        _state = State.Error(throwable)
        return@launch
      }

      _state = State.Ready

      val bitmaps = values.filterNotNull().also { bitmaps = it }

      // updating job
      updatingJob?.cancel()
      updatingJob = launch(Default) {
        _painter = bitmaps[_index].createPainter(filterQuality = filterQuality)
        var timePrevious = System.currentTimeMillis()

        while (true) {
          delay(1)
          val timeNow = System.currentTimeMillis()

          if (!isPlaying) {
            timePrevious = timeNow
            continue
          }

          if (timePrevious + delay < timeNow) {
            timePrevious += delay
            showNext()
          }
        }
      }
    }
  }

  override fun onAbandoned() {
    clear()
  }

  override fun onForgotten() {
    clear()
  }

  private fun clear() {
    context = null
    (_painter as? RememberObserver)?.onForgotten()
    rememberScope?.cancel()
    rememberScope = null
  }

}
