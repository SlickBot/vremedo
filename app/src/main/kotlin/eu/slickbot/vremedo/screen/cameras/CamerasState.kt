package eu.slickbot.vremedo.screen.cameras

import eu.slickbot.arso.model.ArsoCameraData
import eu.slickbot.arso.model.ArsoCameraLength
import eu.slickbot.arso.model.ArsoCameraOrientation

data class CamerasState(
  val isLoading: Boolean = false,
  val orientation: ArsoCameraOrientation? = ArsoCameraOrientation.N,
  val length: ArsoCameraLength = ArsoCameraLength.LONG,
  val cameraData: List<ArsoCameraData> = emptyList(),
  val selectedCameraData: ArsoCameraData? = null,
  val isError: Boolean = false,
  val imageUrls: List<String> = emptyList(),
)
