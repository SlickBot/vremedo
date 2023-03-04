package eu.slickbot.provreme.model

data class ProDay(
    val id: Int,
    val name: String,
    val iconUrl: String,
    val dataList: List<ProData>
) {

    val minTemperatureText: String
        get() = dataList[0].text

    val minTemperature: String
        get() = minTemperatureText.removeSuffix("°C")

    val maxTemperatureText: String
        get() = dataList[1].text

    val maxTemperature: String
        get() = maxTemperatureText.removeSuffix("°C")

}
