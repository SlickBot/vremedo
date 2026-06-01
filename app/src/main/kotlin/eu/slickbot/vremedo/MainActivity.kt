package eu.slickbot.vremedo

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalView
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import eu.slickbot.vremedo.composable.AppBackgroundBox
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

      val isNight by remember { weatherRepository.isNightFlow(DEFAULT_LOCATION) }.collectAsStateWithLifecycle(null)
      val currentScreen by appNavigation.screen.collectAsState()

      val view = LocalView.current
      val lightStatusBarIcons = isNight == false && currentScreen == Screen.Weather
      SideEffect {
        val window = (view.context as ComponentActivity).window
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = lightStatusBarIcons
      }

      VremedoTheme(
        darkTheme = isNight ?: isSystemInDarkTheme(),
      ) {
        AppBackgroundBox(isNight = isNight) {
          NavHost(
            navController = navController,
            startDestination = Screen.Weather.route,
            enterTransition = { fadeIn(animationSpec = tween(350)) },
            exitTransition = { fadeOut(animationSpec = tween(350)) },
          ) {
            screen(Screen.Weather)
            screen(Screen.Aladin)
            screen(Screen.Radar)
            screen(Screen.Satellite)
            screen(Screen.Cameras)
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
