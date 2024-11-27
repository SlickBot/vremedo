package eu.slickbot.provreme.model

internal data class Column(
  val id: Int?,
  val title: String,
  val iconUrl: String,
  val dataList: List<ProData>
)
