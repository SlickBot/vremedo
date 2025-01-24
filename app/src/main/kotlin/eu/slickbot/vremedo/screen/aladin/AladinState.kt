package eu.slickbot.vremedo.screen.aladin

import eu.slickbot.arso.model.ArsoAladinMode
import eu.slickbot.arso.model.ArsoAladinScope

data class AladinState(
  val isLoading: Boolean = false,
  val scope: ArsoAladinScope = ArsoAladinScope.SLOVENIA,
  val mode: ArsoAladinMode = ArsoAladinMode.RAIN,
  val imageUrls: List<String> = emptyList(),
)
