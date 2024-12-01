package eu.slickbot.vremedo.utils

import androidx.navigation.*
import eu.slickbot.vremedo.screen.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppNavigation() {

  private lateinit var navController: NavHostController

  private val _screen = MutableStateFlow<Screen>(Screen.Weather)
  val screen = _screen.asStateFlow()

  private val scope = CoroutineScope(Dispatchers.Main)
  private var job: Job? = null

  fun bind(controller: NavHostController) {
    navController = controller
    job?.cancel()
    job = scope.launch {
      navController.currentBackStackEntryFlow
        .collect { backStackEntry ->
          _screen.update { screen ->
            when (backStackEntry.destination.route) {
              Screen.Splash.route -> Screen.Splash
              Screen.Weather.route -> Screen.Weather
              Screen.Images.route -> Screen.Images
              else -> screen
            }
          }
        }
    }
  }

  fun navigateToSplash() {
    if (screen.value == Screen.Splash) return
    navigate(Screen.Splash)
  }

  fun navigateToWeather() {
    if (screen.value == Screen.Weather) return
    navigate(Screen.Weather) {
      popUpToScreen(Screen.Splash) { inclusive = true }
      launchSingleTop = true
    }
  }

  fun navigateToImages() {
    if (screen.value == Screen.Images) return
    navigate(Screen.Images) {
      popUpToScreen(Screen.Splash) { inclusive = true }
      launchSingleTop = true
    }
  }

  /* Helpers */

  private fun navigate(
    screen: Screen,
    extras: Navigator.Extras? = null,
    options: NavOptionsBuilder.() -> Unit = {},
  ) {
    navController.navigate(screen.route, navOptions(options), extras)
    _screen.value = screen
  }

  private fun NavOptionsBuilder.popUpToScreen(
    screen: Screen,
    builder: PopUpToBuilder.() -> Unit = {},
  ) {
    popUpTo(screen.route, builder)
  }

}
