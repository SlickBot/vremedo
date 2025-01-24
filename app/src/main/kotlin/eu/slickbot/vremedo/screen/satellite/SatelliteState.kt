package eu.slickbot.vremedo.screen.satellite

import eu.slickbot.arso.model.ArsoSatelliteLength
import eu.slickbot.arso.model.ArsoSatelliteScope

data class SatelliteState(
  val isLoading: Boolean = false,
  val scope: ArsoSatelliteScope = ArsoSatelliteScope.SLOVENIA,
  val length: ArsoSatelliteLength = ArsoSatelliteLength.LONG,
  val imageUrls: List<String> = emptyList(),
)
