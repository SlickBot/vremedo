package eu.slickbot.vremedo.utils

import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.Navigator
import androidx.navigation.PopUpToBuilder
import androidx.navigation.navOptions
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
              Screen.Weather.route -> Screen.Weather
              Screen.Images.route -> Screen.Images
              Screen.Aladin.route -> Screen.Aladin
              Screen.Radar.route -> Screen.Radar
              Screen.Satellite.route -> Screen.Satellite
              Screen.Cameras.route -> Screen.Cameras
              else -> screen
            }
          }
        }
    }
  }

  fun navigateToWeather() {
    if (screen.value == Screen.Weather) return
    navigate(Screen.Weather) {
      popUpToScreen(Screen.Weather)
      launchSingleTop = true
    }
  }

  fun navigateToImages() {
    if (screen.value == Screen.Images) return
    navigate(Screen.Images) {
      launchSingleTop = true
    }
  }

  fun navigateToAladin() {
    if (screen.value == Screen.Aladin) return
    navigate(Screen.Aladin) {
      launchSingleTop = true
    }
  }

  fun navigateToRadar() {
    if (screen.value == Screen.Radar) return
    navigate(Screen.Radar) {
      launchSingleTop = true
    }
  }

  fun navigateToSatellite() {
    if (screen.value == Screen.Satellite) return
    navigate(Screen.Satellite) {
      launchSingleTop = true
    }
  }

  fun navigateToCameras() {
    if (screen.value == Screen.Cameras) return
    navigate(Screen.Cameras) {
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
