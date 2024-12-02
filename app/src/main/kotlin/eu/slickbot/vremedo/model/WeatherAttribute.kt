package eu.slickbot.vremedo.model

import androidx.compose.ui.graphics.vector.ImageVector

data class WeatherAttribute(
  val imageVector: ImageVector,
  val value: String?,
  val label: String,
)
