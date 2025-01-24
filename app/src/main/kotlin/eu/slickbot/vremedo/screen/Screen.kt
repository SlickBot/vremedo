package eu.slickbot.vremedo.screen

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import eu.slickbot.vremedo.screen.aladin.AladinScreen
import eu.slickbot.vremedo.screen.cameras.CamerasScreen
import eu.slickbot.vremedo.screen.images.ImagesScreen
import eu.slickbot.vremedo.screen.radar.RadarScreen
import eu.slickbot.vremedo.screen.satellite.SatelliteScreen
import eu.slickbot.vremedo.screen.weather.WeatherScreen

sealed class Screen(
  val screen: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) {
  val route: String = javaClass.simpleName

  data object Weather : Screen({ WeatherScreen() })
  data object Images : Screen({ ImagesScreen() })
  data object Aladin : Screen({ AladinScreen() })
  data object Radar : Screen({ RadarScreen() })
  data object Satellite : Screen({ SatelliteScreen() })
  data object Cameras : Screen({ CamerasScreen() })

}
