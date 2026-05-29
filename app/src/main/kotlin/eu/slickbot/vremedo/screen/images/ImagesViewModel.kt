package eu.slickbot.vremedo.screen.images

import eu.slickbot.vremedo.utils.AppNavigation
import androidx.lifecycle.ViewModel

class ImagesViewModel(
  private val navigation: AppNavigation,
) : ViewModel() {

  fun onAladinClick() {
    navigation.navigateToAladin()
  }

  fun onRadarClick() {
    navigation.navigateToRadar()
  }

  fun onSatelliteClick() {
    navigation.navigateToSatellite()
  }

  fun onCamerasClick() {
    navigation.navigateToCameras()
  }


}
