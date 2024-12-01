package eu.slickbot.vremedo.screen

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import eu.slickbot.vremedo.screen.images.ImagesScreen
import eu.slickbot.vremedo.screen.splash.SplashScreen
import eu.slickbot.vremedo.screen.weather.WeatherScreen

sealed class Screen(
  val screen: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) {
  val route: String = javaClass.simpleName

  data object Splash : Screen({ SplashScreen() })
  data object Weather : Screen({ WeatherScreen() })
  data object Images : Screen({ ImagesScreen() })
//  data object Image : Screen({ ImageScreen() })

}
