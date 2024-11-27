package eu.slickbot.vremedo.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
  primary = Purple80,
  secondary = PurpleGrey80,
  tertiary = Pink80,

  onBackground = Color.White,
)

private val LightColorScheme = lightColorScheme(
  primary = Purple40,
  secondary = PurpleGrey40,
  tertiary = Pink40,

  onBackground = Color.Black,

  /* Other default colors to override
  background = Color(0xFFFFFBFE),
  surface = Color(0xFFFFFBFE),
  onPrimary = Color.White,
  onSecondary = Color.White,
  onTertiary = Color.White,
  onBackground = Color(0xFF1C1B1F),
  onSurface = Color(0xFF1C1B1F),
  */
)

@Composable
fun VremedoTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  lightStatusBar: Boolean = darkTheme,
  lightNavigationBar: Boolean = darkTheme,
  fitsSystemWindows: Boolean = true,
  content: @Composable () -> Unit,
) {
  val view = LocalView.current
  val activity = view.context as Activity

  val dynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
  val colorScheme = when {
    dynamicColor && darkTheme -> dynamicDarkColorScheme(LocalContext.current)
    dynamicColor && !darkTheme -> dynamicLightColorScheme(LocalContext.current)
    darkTheme -> DarkColorScheme
    else -> LightColorScheme
  }

  if (!view.isInEditMode) {
    SideEffect {
      val systemBarsColor = if (fitsSystemWindows) colorScheme.primary else Color.Transparent
      (view.context as Activity).window.statusBarColor = systemBarsColor.toArgb()
      (view.context as Activity).window.navigationBarColor = systemBarsColor.toArgb()
      WindowCompat.setDecorFitsSystemWindows(activity.window, fitsSystemWindows)
      WindowCompat.getInsetsController(activity.window, view).isAppearanceLightStatusBars =
        lightStatusBar
      WindowCompat.getInsetsController(activity.window, view).isAppearanceLightNavigationBars =
        lightNavigationBar
    }
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content,
  )
}
