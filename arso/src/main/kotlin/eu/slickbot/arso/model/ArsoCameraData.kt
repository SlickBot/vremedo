package eu.slickbot.arso.model

data class ArsoCameraData(
  val id: String,
  val meteoSiId: String,
  val crs: String,
  val crsArg: String,
  val icon: String,
  val urls: List<String>,
  val title: String,
  val orientations: List<ArsoCameraOrientation>,
)
