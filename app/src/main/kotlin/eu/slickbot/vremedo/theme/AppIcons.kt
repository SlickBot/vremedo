package eu.slickbot.vremedo.theme

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.EvenOdd
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeCap.Companion.Round
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.App get() = AppIcons

object AppIcons {

  val Temperature: ImageVector by lazy {
    Builder(
      name = "Temperature",
      defaultWidth = 800.0.dp, defaultHeight = 800.0.dp,
      viewportWidth = 24.0f, viewportHeight = 24.0f,
    ).apply {
      path(
        fill = SolidColor(Color(0x00000000)),
        stroke = SolidColor(Color(0xFF000000)),
        strokeLineWidth = 2.0f,
        strokeLineCap = Round,
        strokeLineJoin = StrokeJoin.Companion.Round,
        strokeLineMiter = 4.0f,
        pathFillType = NonZero,
      ) {
        moveTo(12.0f, 15.9998f)
        curveTo(11.4477f, 15.9998f, 11.0f, 16.4475f, 11.0f, 16.9998f)
        curveTo(11.0f, 17.5521f, 11.4477f, 17.9998f, 12.0f, 17.9998f)
        curveTo(12.5523f, 17.9998f, 13.0f, 17.5521f, 13.0f, 16.9998f)
        curveTo(13.0f, 16.4475f, 12.5523f, 15.9998f, 12.0f, 15.9998f)
        close()
        moveTo(12.0f, 15.9998f)
        verticalLineTo(12.0f)
        moveTo(12.0f, 16.9998f)
        lineTo(12.0071f, 17.0069f)
        moveTo(16.0f, 16.9998f)
        curveTo(16.0f, 19.209f, 14.2091f, 20.9998f, 12.0f, 20.9998f)
        curveTo(9.7909f, 20.9998f, 8.0f, 19.209f, 8.0f, 16.9998f)
        curveTo(8.0f, 15.9854f, 8.3776f, 15.0591f, 9.0f, 14.354f)
        lineTo(9.0f, 6.0f)
        curveTo(9.0f, 4.3432f, 10.3431f, 3.0f, 12.0f, 3.0f)
        curveTo(13.6569f, 3.0f, 15.0f, 4.3432f, 15.0f, 6.0f)
        verticalLineTo(14.354f)
        curveTo(15.6224f, 15.0591f, 16.0f, 15.9854f, 16.0f, 16.9998f)
        close()
      }
    }.build()
  }

  val Direction: ImageVector by lazy {
    Builder(
      name = "Direction",
      defaultWidth = 800.0.dp, defaultHeight = 800.0.dp,
      viewportWidth = 24.0f, viewportHeight = 24.0f,
    ).apply {
      path(
        fill = SolidColor(Color(0x00000000)),
        stroke = SolidColor(Color(0xFF464455)),
        strokeLineWidth = 1.0f,
        strokeLineCap = Round,
        strokeLineJoin = StrokeJoin.Companion.Round,
        strokeLineMiter = 4.0f,
        pathFillType = NonZero,
      ) {
        moveTo(18.9762f, 5.5914f)
        lineTo(14.6089f, 18.6932f)
        curveTo(14.4726f, 19.1023f, 13.8939f, 19.1023f, 13.7575f, 18.6932f)
        lineTo(11.7868f, 12.7808f)
        curveTo(11.6974f, 12.5129f, 11.4871f, 12.3026f, 11.2192f, 12.2132f)
        lineTo(5.3068f, 10.2425f)
        curveTo(4.8977f, 10.1061f, 4.8977f, 9.5274f, 5.3068f, 9.3911f)
        lineTo(18.4086f, 5.0238f)
        curveTo(18.7594f, 4.9069f, 19.0931f, 5.2406f, 18.9762f, 5.5914f)
        close()
      }
    }.build()
  }

  val Humidity: ImageVector by lazy {
    Builder(
      name = "Humidity",
      defaultWidth = 800.0.dp, defaultHeight = 800.0.dp,
      viewportWidth = 24.0f, viewportHeight = 24.0f,
    ).apply {
      path(
        fill = SolidColor(Color(0xFF000000)),
        stroke = null,
        strokeLineWidth = 0.0f,
        strokeLineCap = Butt,
        strokeLineJoin = Miter,
        strokeLineMiter = 4.0f,
        pathFillType = NonZero,
      ) {
        moveTo(12.0f, 22.0f)
        curveToRelative(2.579f, 0.0f, 4.0f, -1.35f, 4.0f, -3.8f)
        curveToRelative(0.0f, -3.243f, -3.237f, -5.871f, -3.375f, -5.981f)
        arcToRelative(1.0f, 1.0f, 0.0f, false, false, -1.25f, 0.0f)
        curveTo(11.237f, 12.329f, 8.0f, 14.957f, 8.0f, 18.2f)
        curveTo(8.0f, 20.65f, 9.421f, 22.0f, 12.0f, 22.0f)
        close()
        moveTo(12.0f, 14.361f)
        arcTo(6.153f, 6.153f, 0.0f, false, true, 14.0f, 18.2f)
        curveToRelative(0.0f, 1.112f, -0.335f, 1.8f, -2.0f, 1.8f)
        reflectiveCurveToRelative(-2.0f, -0.688f, -2.0f, -1.8f)
        arcTo(6.153f, 6.153f, 0.0f, false, true, 12.0f, 14.361f)
        close()
        moveTo(6.625f, 2.219f)
        arcToRelative(1.0f, 1.0f, 0.0f, false, false, -1.25f, 0.0f)
        curveTo(5.237f, 2.329f, 2.0f, 4.957f, 2.0f, 8.2f)
        curveTo(2.0f, 10.65f, 3.421f, 12.0f, 6.0f, 12.0f)
        reflectiveCurveToRelative(4.0f, -1.35f, 4.0f, -3.8f)
        curveTo(10.0f, 4.957f, 6.763f, 2.329f, 6.625f, 2.219f)
        close()
        moveTo(6.0f, 10.0f)
        curveToRelative(-1.665f, 0.0f, -2.0f, -0.688f, -2.0f, -1.8f)
        arcTo(6.153f, 6.153f, 0.0f, false, true, 6.0f, 4.361f)
        arcTo(6.153f, 6.153f, 0.0f, false, true, 8.0f, 8.2f)
        curveTo(8.0f, 9.312f, 7.665f, 10.0f, 6.0f, 10.0f)
        close()
        moveTo(18.625f, 2.219f)
        arcToRelative(1.0f, 1.0f, 0.0f, false, false, -1.25f, 0.0f)
        curveTo(17.237f, 2.329f, 14.0f, 4.957f, 14.0f, 8.2f)
        curveToRelative(0.0f, 2.45f, 1.421f, 3.8f, 4.0f, 3.8f)
        reflectiveCurveToRelative(4.0f, -1.35f, 4.0f, -3.8f)
        curveTo(22.0f, 4.957f, 18.763f, 2.329f, 18.625f, 2.219f)
        close()
        moveTo(18.0f, 10.0f)
        curveToRelative(-1.665f, 0.0f, -2.0f, -0.688f, -2.0f, -1.8f)
        arcToRelative(6.153f, 6.153f, 0.0f, false, true, 2.0f, -3.839f)
        arcTo(6.153f, 6.153f, 0.0f, false, true, 20.0f, 8.2f)
        curveTo(20.0f, 9.312f, 19.665f, 10.0f, 18.0f, 10.0f)
        close()
      }
    }.build()
  }

  val Rain: ImageVector by lazy {
    Builder(
      name = "Rain",
      defaultWidth = 800.0.dp, defaultHeight = 800.0.dp,
      viewportWidth = 32.0f, viewportHeight = 32.0f,
    ).apply {
      path(
        fill = SolidColor(Color(0xFF000000)),
        stroke = SolidColor(Color(0x00000000)),
        strokeLineWidth = 1.0f,
        strokeLineCap = Butt,
        strokeLineJoin = Miter,
        strokeLineMiter = 4.0f,
        pathFillType = EvenOdd,
      ) {
        moveTo(19.0f, 27.0f)
        curveTo(18.448f, 27.0f, 18.0f, 27.447f, 18.0f, 28.0f)
        lineTo(18.0f, 31.0f)
        curveTo(18.0f, 31.553f, 18.448f, 32.0f, 19.0f, 32.0f)
        curveTo(19.552f, 32.0f, 20.0f, 31.553f, 20.0f, 31.0f)
        lineTo(20.0f, 28.0f)
        curveTo(20.0f, 27.447f, 19.552f, 27.0f, 19.0f, 27.0f)
        lineTo(19.0f, 27.0f)
        close()
        moveTo(23.067f, 5.028f)
        curveTo(21.599f, 2.053f, 18.543f, 0.0f, 15.0f, 0.0f)
        curveTo(10.25f, 0.0f, 6.37f, 3.682f, 6.033f, 8.345f)
        curveTo(2.542f, 9.34f, 0.0f, 12.39f, 0.0f, 16.0f)
        curveTo(0.0f, 20.26f, 3.54f, 23.731f, 8.0f, 23.977f)
        curveTo(8.0f, 23.977f, 22.331f, 24.0f, 22.5f, 24.0f)
        curveTo(27.747f, 24.0f, 32.0f, 19.747f, 32.0f, 14.5f)
        curveTo(32.0f, 9.445f, 28.048f, 5.323f, 23.067f, 5.028f)
        lineTo(23.067f, 5.028f)
        close()
        moveTo(25.0f, 27.0f)
        curveTo(24.448f, 27.0f, 24.0f, 27.447f, 24.0f, 28.0f)
        lineTo(24.0f, 31.0f)
        curveTo(24.0f, 31.553f, 24.448f, 32.0f, 25.0f, 32.0f)
        curveTo(25.552f, 32.0f, 26.0f, 31.553f, 26.0f, 31.0f)
        lineTo(26.0f, 28.0f)
        curveTo(26.0f, 27.447f, 25.552f, 27.0f, 25.0f, 27.0f)
        lineTo(25.0f, 27.0f)
        close()
        moveTo(7.0f, 27.0f)
        curveTo(6.448f, 27.0f, 6.0f, 27.447f, 6.0f, 28.0f)
        lineTo(6.0f, 31.0f)
        curveTo(6.0f, 31.553f, 6.448f, 32.0f, 7.0f, 32.0f)
        curveTo(7.552f, 32.0f, 8.0f, 31.553f, 8.0f, 31.0f)
        lineTo(8.0f, 28.0f)
        curveTo(8.0f, 27.447f, 7.552f, 27.0f, 7.0f, 27.0f)
        lineTo(7.0f, 27.0f)
        close()
        moveTo(13.0f, 27.0f)
        curveTo(12.448f, 27.0f, 12.0f, 27.447f, 12.0f, 28.0f)
        lineTo(12.0f, 31.0f)
        curveTo(12.0f, 31.553f, 12.448f, 32.0f, 13.0f, 32.0f)
        curveTo(13.552f, 32.0f, 14.0f, 31.553f, 14.0f, 31.0f)
        lineTo(14.0f, 28.0f)
        curveTo(14.0f, 27.447f, 13.552f, 27.0f, 13.0f, 27.0f)
        lineTo(13.0f, 27.0f)
        close()
      }
    }.build()
  }

  val Snow: ImageVector by lazy {
    Builder(
      name = "Snow",
      defaultWidth = 800.0.dp, defaultHeight = 800.0.dp,
      viewportWidth = 24.0f, viewportHeight = 24.0f,
    ).apply {
      path(
        fill = SolidColor(Color(0x00000000)),
        stroke = SolidColor(Color(0xFF000000)),
        strokeLineWidth = 2.0f,
        strokeLineCap = Round,
        strokeLineJoin = StrokeJoin.Companion.Round,
        strokeLineMiter = 4.0f,
        pathFillType = NonZero,
      ) {
        moveTo(12.0f, 3.0f)
        verticalLineTo(21.0f)
        moveTo(16.0f, 4.0f)
        lineTo(12.0f, 8.0f)
        lineTo(8.0088f, 4.0f)
        moveTo(8.0088f, 20.0f)
        lineTo(12.0f, 16.0f)
        lineTo(16.0f, 20.0f)
        moveTo(3.0f, 12.0f)
        horizontalLineTo(21.0f)
        moveTo(4.0f, 8.0f)
        lineTo(8.0088f, 12.0f)
        lineTo(4.0f, 16.0f)
        moveTo(20.0f, 16.0f)
        lineTo(16.0f, 12.0f)
        lineTo(20.0f, 8.0f)
      }
    }.build()
  }

  val Wind: ImageVector by lazy {
    Builder(
      name = "Wind",
      defaultWidth = 800.0.dp, defaultHeight = 800.0.dp,
      viewportWidth = 24.0f, viewportHeight = 24.0f,
    ).apply {
      path(
        fill = SolidColor(Color(0x00000000)),
        stroke = SolidColor(Color(0xFF000000)),
        strokeLineWidth = 2.0f,
        strokeLineCap = Round,
        strokeLineJoin = StrokeJoin.Companion.Round,
        strokeLineMiter = 4.0f,
        pathFillType = NonZero,
      ) {
        moveTo(15.7639f, 7.0f)
        curveTo(16.3132f, 6.3863f, 17.1115f, 6.0f, 18.0f, 6.0f)
        curveTo(19.6569f, 6.0f, 21.0f, 7.3432f, 21.0f, 9.0f)
        curveTo(21.0f, 10.6569f, 19.6569f, 12.0f, 18.0f, 12.0f)
        horizontalLineTo(3.0f)
        moveTo(8.5093f, 4.6667f)
        curveTo(8.8755f, 4.2575f, 9.4077f, 4.0f, 10.0f, 4.0f)
        curveTo(11.1046f, 4.0f, 12.0f, 4.8954f, 12.0f, 6.0f)
        curveTo(12.0f, 7.1046f, 11.1046f, 8.0f, 10.0f, 8.0f)
        horizontalLineTo(3.0f)
        moveTo(11.5093f, 19.3333f)
        curveTo(11.8755f, 19.7425f, 12.4077f, 20.0f, 13.0f, 20.0f)
        curveTo(14.1046f, 20.0f, 15.0f, 19.1046f, 15.0f, 18.0f)
        curveTo(15.0f, 16.8954f, 14.1046f, 16.0f, 13.0f, 16.0f)
        horizontalLineTo(3.0f)
      }
    }.build()
  }

  val Gauge: ImageVector by lazy {
    Builder(
      name = "Gauge",
      defaultWidth = 800.0.dp, defaultHeight = 800.0.dp,
      viewportWidth = 24.0f, viewportHeight = 24.0f,
    ).apply {
      path(
        fill = SolidColor(Color(0x00000000)),
        stroke = SolidColor(Color(0xFF000000)),
        strokeLineWidth = 2.0f,
        strokeLineCap = Round,
        strokeLineJoin = StrokeJoin.Companion.Round,
        strokeLineMiter = 4.0f,
        pathFillType = NonZero,
      ) {
        moveTo(15.0f, 5.5121f)
        curveTo(18.4956f, 6.7477f, 21.0f, 10.0814f, 21.0f, 14.0f)
        curveTo(21.0f, 15.4685f, 20.6483f, 16.8549f, 20.0245f, 18.0794f)
        curveTo(19.7216f, 18.6741f, 19.0839f, 19.0f, 18.4165f, 19.0f)
        horizontalLineTo(5.5835f)
        curveTo(4.9161f, 19.0f, 4.2784f, 18.6741f, 3.9755f, 18.0794f)
        curveTo(3.3517f, 16.8549f, 3.0f, 15.4685f, 3.0f, 14.0f)
        curveTo(3.0f, 10.0814f, 5.5044f, 6.7477f, 9.0f, 5.5121f)
        moveTo(12.0f, 12.9999f)
        curveTo(11.4477f, 12.9999f, 11.0f, 13.4477f, 11.0f, 13.9999f)
        curveTo(11.0f, 14.5522f, 11.4477f, 14.9999f, 12.0f, 14.9999f)
        curveTo(12.5523f, 14.9999f, 13.0f, 14.5522f, 13.0f, 13.9999f)
        curveTo(13.0f, 13.4477f, 12.5523f, 12.9999f, 12.0f, 12.9999f)
        close()
        moveTo(12.0f, 12.9999f)
        verticalLineTo(3.9999f)
      }
    }.build()
  }

  val Visibility: ImageVector by lazy {
    Builder(
      name = "Visibility",
      defaultWidth = 800.0.dp, defaultHeight = 800.0.dp,
      viewportWidth = 24.0f, viewportHeight = 24.0f,
    ).apply {
      path(
        fill = SolidColor(Color(0xFF000000)),
        stroke = null,
        strokeLineWidth = 0.0f,
        strokeLineCap = Butt,
        strokeLineJoin = Miter,
        strokeLineMiter = 4.0f,
        pathFillType = NonZero,
      ) {
        moveTo(12.0f, 4.5f)
        curveTo(7.0f, 4.5f, 2.73f, 7.61f, 1.0f, 12.0f)
        curveToRelative(1.73f, 4.39f, 6.0f, 7.5f, 11.0f, 7.5f)
        reflectiveCurveToRelative(9.27f, -3.11f, 11.0f, -7.5f)
        curveToRelative(-1.73f, -4.39f, -6.0f, -7.5f, -11.0f, -7.5f)
        close()
        moveTo(12.0f, 17.0f)
        curveToRelative(-2.76f, 0.0f, -5.0f, -2.24f, -5.0f, -5.0f)
        reflectiveCurveToRelative(2.24f, -5.0f, 5.0f, -5.0f)
        reflectiveCurveToRelative(5.0f, 2.24f, 5.0f, 5.0f)
        reflectiveCurveToRelative(-2.24f, 5.0f, -5.0f, 5.0f)
        close()
        moveTo(12.0f, 9.0f)
        curveToRelative(-1.66f, 0.0f, -3.0f, 1.34f, -3.0f, 3.0f)
        reflectiveCurveToRelative(1.34f, 3.0f, 3.0f, 3.0f)
        reflectiveCurveToRelative(3.0f, -1.34f, 3.0f, -3.0f)
        reflectiveCurveToRelative(-1.34f, -3.0f, -3.0f, -3.0f)
        close()
      }
    }.build()
  }

}
