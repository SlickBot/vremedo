package eu.slickbot.vremedo.model

data class WeatherHours(
  val name: String,
  val iconUrl: String,
  val dataList: List<WeatherData>,
) {

  val startHour: Int
    get() = name.split(" - ")[0].removeSuffix("h").toInt()

  val endHour: Int
    get() = name.split(" - ")[1].removeSuffix("h").toInt()


  val temperatureText: String?
    get() = dataList.firstOrNull { it.title.startsWith("Temperatura") }?.text

  val temperature: Float?
    get() = temperatureText?.removeSuffix("°C")?.toFloat()


  val windSpeedText: String?
    get() = dataList.firstOrNull { it.title.equals("Hitrost vetra:", ignoreCase = true) }?.text

  val windSpeed: Int?
    get() = windSpeedText?.removeSuffix(" m/s")?.toIntOrNull()


  val windDirectionText: String?
    get() = dataList.firstOrNull { it.title.equals("Smer vetra:", ignoreCase = true) }?.text

  val windDirection: Int?
    get() = windDirectionText?.removeSuffix("°")?.toIntOrNull()

}
