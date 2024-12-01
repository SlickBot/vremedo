package eu.slickbot.vremedo

import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import eu.slickbot.vremedo.composable.BackgroundBox
import eu.slickbot.vremedo.repository.WeatherRepository
import eu.slickbot.vremedo.screen.Screen
import eu.slickbot.vremedo.theme.VremedoTheme
import eu.slickbot.vremedo.utils.AppLifecycle
import eu.slickbot.vremedo.utils.AppNavigation
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

  private val appLifecycle: AppLifecycle by inject()
  private val appNavigation: AppNavigation by inject()
  private val weatherRepository: WeatherRepository by inject()

  @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
  override fun onCreate(savedInstanceState: Bundle?) {

    super.onCreate(savedInstanceState)
    appLifecycle.bind(this)

    installSplashScreen().apply {
      setOnExitAnimationListener { splashScreenView ->
        ObjectAnimator.ofFloat(splashScreenView.view, View.ALPHA, 1f, 0f).apply {
          duration = 300
          doOnEnd { splashScreenView.remove() }
        }.start()
      }
    }

    enableEdgeToEdge()

    setContent {
      val navController = rememberNavController()
      appNavigation.bind(navController)

      val isNight by remember { weatherRepository.isNightFlow("Novo mesto") }.collectAsStateWithLifecycle(null)
      VremedoTheme(
        darkTheme = isNight ?: isSystemInDarkTheme(),
      ) {
        BackgroundBox(isNight = isNight) {
          NavHost(
            navController = navController,
            startDestination = Screen.Weather.route,
          ) {
            screen(Screen.Splash)
            screen(Screen.Weather)
            screen(Screen.Images)
//            screen(Screen.Image)
          }
        }
      }
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    appLifecycle.unbind(this)
  }

  /* Helpers */

  private fun NavGraphBuilder.screen(
    screen: Screen,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
  ) {
    composable(screen.route, arguments, deepLinks, content = screen.screen)
  }

}
