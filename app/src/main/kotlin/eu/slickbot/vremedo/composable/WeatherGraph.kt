package eu.slickbot.vremedo.composable

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Path
import android.graphics.PointF
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.*
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import coil.ImageLoader
import coil.request.CachePolicy
import eu.slickbot.vremedo.R
import eu.slickbot.vremedo.extension.*
import eu.slickbot.vremedo.model.WeatherItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import android.graphics.Paint as AndroidPaint

@Composable
fun rememberWeatherGraphState(): WeatherGraphState {
  return remember { WeatherGraphState() }
}

@Stable
class WeatherGraphState {

  var _onScrollTo: (Int) -> Unit = {}

  val scroll = ScrollState(initial = 0)

  var percentage: Float by mutableStateOf(0f)
    internal set

  var currentIndex: Int by mutableStateOf(0)
    internal set


  suspend fun scrollTo(percentage: Float) {
    val value = scroll.maxValue * percentage
    scroll.scrollTo(value.roundToInt())
  }

  suspend fun animateScrollTo(percentage: Float, animationSpec: AnimationSpec<Float> = tween()) {
    val value = scroll.maxValue * percentage
    scroll.animateScrollTo(value.roundToInt(), animationSpec)
  }

}

// https://proandroiddev.com/creating-graph-in-jetpack-compose-312957b11b2
@Composable
fun WeatherGraph(
  items: List<WeatherItem>,
  state: WeatherGraphState,
  valueMin: Float,
  valueMax: Float,
  weatherImageSize: DpSize,
  windImageSize: DpSize,
  itemSpacing: Dp,
  lineOffset: Dp,
  modifier: Modifier = Modifier,
  paddingValues: PaddingValues = PaddingValues(0.dp),
) {
  if (items.isEmpty())
    return

  val scope = rememberCoroutineScope()
  val context = LocalContext.current

  // scroll to start when values change
  LaunchedEffect(items) {
    state.scroll.scrollTo(0)
  }

  val onBackgroundColor = MaterialTheme.colorScheme.onBackground.toArgb()

  val xLabelsPaint = rememberAndroidPaint {
    textSize = 26f
    color = onBackgroundColor
  }
  val yLabelsPaint = rememberAndroidPaint {
    textSize = 40f
    color = onBackgroundColor
  }
  val temperaturesPaint = rememberAndroidPaint {
    textSize = 40f
    color = onBackgroundColor
  }

  val imageLoader = remember {
    ImageLoader.Builder(context)
      .diskCachePolicy(CachePolicy.DISABLED)
//      .memoryCachePolicy(CachePolicy.DISABLED)
      .build()
  }

  val bitmaps = remember { mutableStateMapOf<String, ImageBitmap>() }
  LaunchedEffect(items) {
    scope.launch(Dispatchers.IO) {
      for (item in items) {
        bitmaps[item.hours.iconUrl] = imageLoader.getImageBitmap(context, item.hours.iconUrl)
      }
    }
  }

  var arrowBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
  LaunchedEffect(items) {
    arrowBitmap = getBitmapFromVectorDrawable(context, R.drawable.ic_arrow_upward).asImageBitmap()
  }

  val topPadding = paddingValues.calculateTopPadding()
  val bottomPadding = paddingValues.calculateBottomPadding()
  val startPadding = paddingValues.calculateStartPadding(LayoutDirection.Ltr)
  val endPadding = paddingValues.calculateEndPadding(LayoutDirection.Ltr)

  val chartWidth = itemSpacing * (items.size - 1)
  val canvasWidth = chartWidth + startPadding + endPadding

  val chartWidthPx = with(LocalDensity.current) { chartWidth.toPx() }
  val lineOffsetPx =
    with(LocalDensity.current) { state.scroll.value + lineOffset.toPx() - startPadding.toPx() }

  val percentage = if (lineOffsetPx < 0) 0f else lineOffsetPx / chartWidthPx
  if (percentage != state.percentage) {
    state.percentage = percentage
  }

  val index = (items.lastIndex * state.percentage).toInt()
  if (index != state.currentIndex) {
    state.currentIndex = index
  }

  Box(modifier) {
    Box(
      Modifier
        .width(2.dp)
        .fillMaxHeight()
        .offset(x = lineOffset - 1.dp)
        .padding(vertical = 40.dp)
        .background(Color.White.copy(alpha = .1f))
    )

    Box(Modifier.horizontalScroll(state.scroll)) {

      Canvas(
        modifier = Modifier
          .width(canvasWidth)
          .fillMaxHeight()
      ) {

        val chartRect = Rect(
          Offset(startPadding.toPx(), 0f),
          Size(chartWidth.toPx(), size.height)
        )

        // draw hours
        for (i in items.indices) {
          val hour = items[i].dateTime.hour
          if (hour % 2 == 1) continue
          val text = hour.toString()
          val textBounds = xLabelsPaint.getTextBounds(text)
          drawContext.canvas.nativeCanvas.drawText(
            text,
            chartRect.left + (itemSpacing.toPx() * i) - (textBounds.width / 2f),
            size.height,
            xLabelsPaint,
          )
        }

        val coordinates = mutableListOf<Offset>()
        for ((hours, dataIdx) in items.withIndex().groupBy { it.value.hours }) {
          val temperature = hours.temperature ?: continue

          val middleIdx = dataIdx.sumOf { it.index } / dataIdx.size.toFloat()

          coordinates += Offset(
            chartRect.left + (itemSpacing.toPx() * middleIdx),
            chartRect.bottom - temperature.rangeMap(
              valueMin,
              valueMax,
              chartRect.top,
              chartRect.bottom
            )
          )
        }

        val controlPoints1 = mutableListOf<PointF>()
        val controlPoints2 = mutableListOf<PointF>()

        // calculating the connection points
        for (i in 1 until coordinates.size) {
          controlPoints1 += PointF(
            (coordinates[i].x + coordinates[i - 1].x) / 2,
            coordinates[i - 1].y
          )
          controlPoints2 += PointF((coordinates[i].x + coordinates[i - 1].x) / 2, coordinates[i].y)
        }

        val path = AndroidPath().apply {
          reset()
          moveTo(chartRect.left, coordinates.first().y)
          lineTo(coordinates.first().x, coordinates.first().y)
          for (i in 0 until coordinates.size - 1) {
            cubicTo(
              controlPoints1[i].x, controlPoints1[i].y,
              controlPoints2[i].x, controlPoints2[i].y,
              coordinates[i + 1].x, coordinates[i + 1].y
            )
          }
          lineTo(chartRect.right, coordinates.last().y)
        }

        drawPath(
          path,
          color = Color.White.copy(.5f),
          style = Stroke(
            width = 5f,
            cap = StrokeCap.Round,
          )
        )

        // draw path gradient fill
        val fillPath = Path(path.asAndroidPath())
          .asComposePath()
          .apply {
            lineTo(canvasWidth.toPx(), coordinates.last().y)
            lineTo(canvasWidth.toPx(), size.height)
            lineTo(0f, size.height)
            lineTo(0f, coordinates.first().y)
            close()
          }

        drawPath(
          fillPath,
          Brush.verticalGradient(
            0f to Color.Yellow.copy(alpha = .5f),
            1f to Color.Yellow.copy(alpha = .0f),
            endY = chartRect.bottom
          ),
        )

        val hoursSet = items.map { it.hours }.toSet()

        // draw weather icons
        for ((hours, coordinate) in hoursSet.zip(coordinates)) {
          val bitmap = bitmaps[hours.iconUrl] ?: ImageBitmap(1, 1)

          val widthRatio = weatherImageSize.width / bitmap.width
          val heightRatio = weatherImageSize.height / bitmap.height
          val maxRatio = max(widthRatio, heightRatio)
          val width = bitmap.width * maxRatio
          val height = bitmap.height * maxRatio
          val imageResized = DpSize(width, height).toSize()

          drawImage(
            image = bitmap,
            srcOffset = IntOffset(0, 0),
            srcSize = bitmap.size,
            dstOffset = Offset(
              coordinate.x - (imageResized.width * 0.5f),
              coordinate.y - (imageResized.height * 1.5f)
            ).toIntOffset(),
            dstSize = imageResized.toIntSize(),
          )
        }

        // draw temperatures
        for ((hours, coordinate) in hoursSet.zip(coordinates)) {
          val temperature = hours.temperature ?: 0f
          val text = "%d°".format(temperature.roundToInt())
          val textBounds = yLabelsPaint.getTextBounds(text)
          drawContext.canvas.nativeCanvas.drawText(
            text,
            coordinate.x - (textBounds.width * .5f),
            coordinate.y + (textBounds.height * 2.2f),
            temperaturesPaint,
          )
        }

        // draw wind directions
        for ((hours, coordinate) in hoursSet.zip(coordinates)) {
          val bitmap = arrowBitmap ?: continue
          val windDirection = hours.windDirection ?: continue

          val widthRatio = windImageSize.width / bitmap.width
          val heightRatio = windImageSize.height / bitmap.height
          val maxRatio = max(widthRatio, heightRatio)
          val width = bitmap.width * maxRatio
          val height = bitmap.height * maxRatio
          val imageResized = DpSize(width, height).toSize()

          val offset = Offset(
            coordinate.x - (imageResized.width * 0.5f),
            coordinate.y + (imageResized.height * 2f)
          )
          val pivot = Offset(
            offset.x + (width.toPx() / 2),
            offset.y + (height.toPx() / 2),
          )

          rotate(degrees = windDirection.toFloat(), pivot = pivot) {
            drawImage(
              image = bitmap,
              dstOffset = offset.toIntOffset(),
              dstSize = imageResized.toIntSize(),
            )
          }
        }
      }
    }
  }
}

private fun NativeCanvas.drawString(text: String, x: Float, y: Float, textPaint: AndroidPaint) {
  if ("\n" in text) {
    val split = text.split("\n")
    for ((i, part) in split.withIndex()) {
      drawText(part, x, y + (i * textPaint.getTextBounds(part).height), textPaint)
    }
  } else {
    drawText(text, x, y, textPaint)
  }
}

private fun Float.rangeMap(
  inMin: Float, inMax: Float,
  outMin: Float, outMax: Float
): Float {
  return (this - inMin) * (outMax - outMin) / (inMax - inMin) + outMin
}

private fun getBitmapFromVectorDrawable(context: Context, @DrawableRes drawableId: Int): Bitmap {
  val drawable = DrawableCompat.wrap(ContextCompat.getDrawable(context, drawableId)!!).mutate()
  val bitmap = Bitmap.createBitmap(
    drawable.intrinsicWidth,
    drawable.intrinsicHeight,
    Bitmap.Config.ARGB_8888
  )
  val canvas = android.graphics.Canvas(bitmap)
  drawable.setBounds(0, 0, canvas.width, canvas.height)
  drawable.draw(canvas)
  return bitmap
}
