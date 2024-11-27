package eu.slickbot.vremedo.model

data class WeatherData(
  val title: String,
  val text: String,
  val imageUrl: String? = null,
)
