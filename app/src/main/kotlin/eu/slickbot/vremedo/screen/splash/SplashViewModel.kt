package eu.slickbot.vremedo.screen.splash

import androidx.lifecycle.viewModelScope
import eu.slickbot.vremedo.utils.ComponentViewModel
import eu.slickbot.vremedo.utils.AppNavigation
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel(
  private val navigator: AppNavigation,
) : ComponentViewModel() {

  private var splashJob: Job? = null

  override fun onComposableCreate() {
    splashJob?.cancel()
    splashJob = viewModelScope.launch {
//      delay(0)
      navigator.navigateToWeather()
    }
  }

  override fun onComposableDispose() {
    splashJob?.cancel()
  }

}
