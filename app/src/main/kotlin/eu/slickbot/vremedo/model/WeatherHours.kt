package eu.slickbot.vremedo.model

data class WeatherHours(
  val name: String,
  val iconUrl: String,
  val dataList: List<WeatherData>,
) {

  // name looks like "0h - 6h"; tolerate malformed scraped values instead of crashing.
  val startHour: Int?
    get() = name.substringBefore(" - ", "").trim().removeSuffix("h").toIntOrNull()

  val endHour: Int?
    get() = name.substringAfter(" - ", "").trim().removeSuffix("h").toIntOrNull()


  val temperatureText: String?
    get() = dataList.firstOrNull { it.title.startsWith("Temperatura") }?.text

  val temperature: Float?
    get() = temperatureText?.removeSuffix("°C")?.toFloatOrNull()


  val humidityText: String?
    get() = dataList.firstOrNull { it.title.startsWith("Relativna vlažnost") }?.text

  val humidity: Int?
    get() = humidityText?.removeSuffix("%")?.toIntOrNull()


  val windSpeedText: String?
    get() = dataList.firstOrNull { it.title.equals("Hitrost vetra:", ignoreCase = true) }?.text

  val windSpeed: Float?
    get() = windSpeedText?.removeSuffix(" m/s")?.toFloatOrNull()


  val windDirectionText: String?
    get() = dataList.firstOrNull { it.title.equals("Smer vetra:", ignoreCase = true) }?.text

  val windDirection: Int?
    get() = windDirectionText?.removeSuffix("°")?.toIntOrNull()


  val pressureText: String?
    get() = dataList.firstOrNull { it.title.equals("Tlak:", ignoreCase = true) }?.text

  val pressure: Int?
    get() = pressureText?.removeSuffix(" hPa")?.toIntOrNull()


  val rainText: String?
    get() = dataList.firstOrNull { it.title.equals("Padavine:", ignoreCase = true) }?.text

  val rain: Float?
    get() = rainText?.removeSuffix(" mm")?.toFloatOrNull()


  val snowText: String?
    get() = dataList.firstOrNull { it.title.equals("Napoved snega:", ignoreCase = true) }?.text
      ?.replace("m^2", "m²")

  val snow: Float?
    get() = snowText?.removeSuffix(" kg/m^2")?.toFloatOrNull()


  val visibilityText: String?
    get() = dataList.firstOrNull { it.title.equals("Vidljivost:", ignoreCase = true) }?.text

  val visibility: Int?
    get() = visibilityText?.removeSuffix(" m")?.toIntOrNull()

}
