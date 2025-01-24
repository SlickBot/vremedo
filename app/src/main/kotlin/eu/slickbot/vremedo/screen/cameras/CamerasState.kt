package eu.slickbot.vremedo.screen.cameras

import eu.slickbot.arso.model.ArsoCameraData
import eu.slickbot.arso.model.ArsoCameraLength
import eu.slickbot.arso.model.ArsoCameraOrientation

data class CamerasState(
  val isLoading: Boolean = false,
  val orientation: ArsoCameraOrientation = ArsoCameraOrientation.N,
  val length: ArsoCameraLength = ArsoCameraLength.LONG,
  val imageData: List<ArsoCameraData> = emptyList(),
  val selectedImageData: ArsoCameraData? = null,
  val imageUrls: List<String> = emptyList(),
)
