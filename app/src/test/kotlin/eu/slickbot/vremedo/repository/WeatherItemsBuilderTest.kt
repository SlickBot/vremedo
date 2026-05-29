package eu.slickbot.vremedo.repository

import eu.slickbot.vremedo.model.WeatherDay
import eu.slickbot.vremedo.model.WeatherHours
import kotlinx.datetime.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WeatherItemsBuilderTest {

  private val friday = LocalDate(2026, 5, 29)

  private fun day(id: Int, name: String) = WeatherDay(id, name, "", emptyList())
  private fun span(name: String) = WeatherHours(name, "", emptyList())

  @Test
  fun expandsSingleSpanIntoHourlyItems() {
    val days = listOf(day(1, "Petek") to listOf(span("0h - 6h")))

    val items = buildWeatherItems(days, friday, "Petek")

    assertEquals(listOf(0, 1, 2, 3, 4, 5), items.map { it.dateTime.hour })
    assertTrue(items.all { it.dateTime.date == friday })
  }

  @Test
  fun expandsConsecutiveSameDaySpans() {
    val days = listOf(day(1, "Petek") to listOf(span("0h - 6h"), span("6h - 12h")))

    val items = buildWeatherItems(days, friday, "Petek")

    assertEquals((0..11).toList(), items.map { it.dateTime.hour })
    assertTrue(items.all { it.dateTime.date == friday })
  }

  @Test
  fun datesSubsequentDaysRelativeToToday() {
    val days = listOf(
      day(1, "Petek") to listOf(span("0h - 6h")),
      day(2, "Sobota") to listOf(span("0h - 6h")),
    )

    val items = buildWeatherItems(days, friday, "Petek")

    assertEquals(6, items.count { it.dateTime.date == friday })
    assertEquals(6, items.count { it.dateTime.date == LocalDate(2026, 5, 30) })
  }

  @Test
  fun skipsSpansWithUnparseableHours() {
    val days = listOf(day(1, "Petek") to listOf(span("garbage")))
    assertTrue(buildWeatherItems(days, friday, "Petek").isEmpty())
  }

  @Test
  fun emptyInputYieldsEmptyOutput() {
    assertTrue(buildWeatherItems(emptyList(), friday, "Petek").isEmpty())
  }
}
