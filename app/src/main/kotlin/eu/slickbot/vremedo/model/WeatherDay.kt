package eu.slickbot.vremedo.model

data class WeatherDay(
  val id: Int,
  val name: String,
  val iconUrl: String,
  val dataList: List<WeatherData>) {

  val minTemperatureText: String
    get() = dataList[0].text

  val minTemperature: String
    get() = minTemperatureText.removeSuffix("°C")

  val maxTemperatureText: String
    get() = dataList[1].text

  val maxTemperature: String
    get() = maxTemperatureText.removeSuffix("°C")

}
