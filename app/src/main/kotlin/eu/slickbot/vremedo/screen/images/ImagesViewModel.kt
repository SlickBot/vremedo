package eu.slickbot.vremedo.screen.images

import eu.slickbot.vremedo.utils.AppNavigation
import eu.slickbot.vremedo.utils.ComponentViewModel

class ImagesViewModel(
  private val navigation: AppNavigation,
) : ComponentViewModel() {

  override fun onComposableCreate() {

  }

  override fun onComposableDispose() {

  }

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
