package eu.slickbot.provreme.model

data class ProDay(
    val id: Int,
    val name: String,
    val iconUrl: String,
    val dataList: List<ProData>
)
