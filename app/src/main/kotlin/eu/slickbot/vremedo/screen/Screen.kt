package eu.slickbot.vremedo.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import eu.slickbot.vremedo.screen.splash.SplashScreen
import eu.slickbot.vremedo.screen.weather.WeatherScreen

sealed class Screen(
    val screen: @Composable (NavBackStackEntry) -> Unit
) {
    val route: String = javaClass.simpleName

    object Splash : Screen({ SplashScreen() })
    object Weather : Screen({ WeatherScreen() })
//    object Images : Screen({ ImagesScreen() })
//    object Image : Screen({ ImageScreen() })

}
