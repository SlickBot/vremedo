package eu.slickbot.vremedo.composable

import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.DefaultGroupName
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object CustomIcons {

  val Compass by lazyImageVector(
    name = "Compass",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f,
  ) {
    path(
      fill = null,
      fillAlpha = 1.0f,
      stroke = SolidColor(Color(0xFF000000)),
      strokeAlpha = 1.0f,
      strokeLineWidth = 2f,
      strokeLineCap = StrokeCap.Round,
      strokeLineJoin = StrokeJoin.Round,
      strokeLineMiter = 1.0f,
      pathFillType = PathFillType.NonZero
    ) {
      moveTo(16.24f, 7.76f)
      lineToRelative(-1.804f, 5.411f)
      arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1.265f, 1.265f)
      lineTo(7.76f, 16.24f)
      lineToRelative(1.804f, -5.411f)
      arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.265f, -1.265f)
      close()
    }
    path(
      fill = null,
      fillAlpha = 1.0f,
      stroke = SolidColor(Color(0xFF000000)),
      strokeAlpha = 1.0f,
      strokeLineWidth = 2f,
      strokeLineCap = StrokeCap.Round,
      strokeLineJoin = StrokeJoin.Round,
      strokeLineMiter = 1.0f,
      pathFillType = PathFillType.NonZero
    ) {
      moveTo(22f, 12f)
      arcTo(10f, 10f, 0f, isMoreThanHalf = false, isPositiveArc = true, 12f, 22f)
      arcTo(10f, 10f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2f, 12f)
      arcTo(10f, 10f, 0f, isMoreThanHalf = false, isPositiveArc = true, 22f, 12f)
      close()
    }
  }

  private fun lazyImageVector(
    name: String = DefaultGroupName,
    defaultWidth: Dp,
    defaultHeight: Dp,
    viewportWidth: Float,
    viewportHeight: Float,
    tintColor: Color = Color.Unspecified,
    tintBlendMode: BlendMode = BlendMode.SrcIn,
    autoMirror: Boolean = false,
    builder: ImageVector.Builder.() -> Unit,
  ): Lazy<ImageVector> {
    return lazy {
      ImageVector.Builder(
        name = name,
        defaultWidth = defaultWidth,
        defaultHeight = defaultHeight,
        viewportWidth = viewportWidth,
        viewportHeight = viewportHeight,
        tintColor = tintColor,
        tintBlendMode = tintBlendMode,
        autoMirror = autoMirror,
      ).apply(builder).build()
    }
  }

}
