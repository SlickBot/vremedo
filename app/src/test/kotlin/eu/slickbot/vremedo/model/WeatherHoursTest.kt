package eu.slickbot.vremedo.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class WeatherHoursTest {

  private fun hours(name: String, vararg data: Pair<String, String>): WeatherHours {
    return WeatherHours(
      name = name,
      iconUrl = "",
      dataList = data.map { WeatherData(it.first, it.second, null) },
    )
  }

  @Test
  fun parsesStartAndEndHourFromSpanName() {
    val h = hours("0h - 6h")
    assertEquals(0, h.startHour)
    assertEquals(6, h.endHour)
  }

  @Test
  fun parsesSpanWithoutHSuffix() {
    val h = hours("12 - 18")
    assertEquals(12, h.startHour)
    assertEquals(18, h.endHour)
  }

  @Test
  fun malformedSpanNameYieldsNullInsteadOfCrashing() {
    val h = hours("nonsense")
    assertNull(h.startHour)
    assertNull(h.endHour)
  }

  @Test
  fun parsesNumericAttributes() {
    val h = hours(
      "0h - 6h",
      "Temperatura" to "12.5°C",
      "Relativna vlažnost" to "80%",
      "Hitrost vetra:" to "3.2 m/s",
      "Tlak:" to "1013 hPa",
    )
    assertEquals(12.5f, h.temperature)
    assertEquals(80, h.humidity)
    assertEquals(3.2f, h.windSpeed)
    assertEquals(1013, h.pressure)
  }

  @Test
  fun missingOrJunkAttributesYieldNull() {
    val h = hours("0h - 6h", "Temperatura" to "n/a")
    assertNull(h.temperature)
    assertNull(h.humidity)
  }
}
