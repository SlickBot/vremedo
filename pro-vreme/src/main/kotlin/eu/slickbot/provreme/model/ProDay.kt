package eu.slickbot.provreme.model

data class ProDay(
  val id: Int,
  val name: String,
  val iconUrl: String,
  val data: List<ProData>,
)
