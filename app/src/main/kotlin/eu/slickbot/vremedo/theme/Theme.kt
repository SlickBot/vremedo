package eu.slickbot.vremedo.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

val colorDarkOverlay = Color.Black.copy(alpha = .1f)
val colorLightOverlay = Color.White.copy(alpha = .1f)

val colorDarkPrimary = Color(0xFF282F4C)
val colorLightPrimary = Color(0xFF657B3D)

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
  content: @Composable () -> Unit,
) {
  val useDynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

  val colorScheme = when {
    useDynamicColor && darkTheme -> dynamicDarkColorScheme(LocalContext.current)
    useDynamicColor && !darkTheme -> dynamicLightColorScheme(LocalContext.current)
    darkTheme -> DarkColorScheme
    else -> LightColorScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content,
  )
}
