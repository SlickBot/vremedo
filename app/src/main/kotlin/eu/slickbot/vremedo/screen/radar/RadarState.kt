package eu.slickbot.vremedo.screen.radar

import eu.slickbot.arso.model.ArsoRadarLength
import eu.slickbot.arso.model.ArsoRadarScope

data class RadarState(
  val isLoading: Boolean = false,
  val length: ArsoRadarLength = ArsoRadarLength.LONG,
  val scope: ArsoRadarScope = ArsoRadarScope.NEIGHBOURS,
  val imageUrls: List<String> = emptyList(),
)
