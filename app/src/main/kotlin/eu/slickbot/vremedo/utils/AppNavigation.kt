package eu.slickbot.vremedo.utils

import androidx.navigation.*
import eu.slickbot.vremedo.screen.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppNavigation {

  private lateinit var navController: NavHostController

  private val _screen = MutableStateFlow<Screen>(Screen.Splash)
  val screen = _screen.asStateFlow()

  fun bind(controller: NavHostController) {
    navController = controller
  }

  fun navigateToSplash() {
    navigate(Screen.Splash)
  }

  fun navigateToWeather() {
    navigate(Screen.Weather) {
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
