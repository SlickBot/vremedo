package eu.slickbot.provreme

import eu.slickbot.provreme.model.ProCity
import okhttp3.OkHttpClient
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ProVremeParseTest {

  // parseCities does not touch the network, so a bare client is fine.
  private val proVreme = ProVreme(OkHttpClient())

  private fun fixtureDocument(name: String): Document {
    return Jsoup.parse(javaClass.getResource("/fixtures/$name")!!.readText())
  }

  @Test
  fun parseCitiesKeepsRealOptionsAndDropsFillers() {
    val html = """
      <html><body>
        <select id="profkoKraj">
          <option value="">Izberi kraj</option>
          <option value="------">------</option>
          <option value="100">Ljubljana</option>
          <option value="200">Maribor</option>
          <option value="100">Ljubljana</option>
        </select>
      </body></html>
    """.trimIndent()

    val cities = proVreme.parseCities(Jsoup.parse(html))

    // fillers removed, duplicate Ljubljana de-duplicated
    assertEquals(listOf(ProCity(100, "Ljubljana"), ProCity(200, "Maribor")), cities)
  }

  @Test
  fun parseDaysReturnsSevenForecastDays() {
    val days = proVreme.parseDays(fixtureDocument("week.html"))

    assertEquals(7, days.size)
    assertEquals(1, days[0].id)
    assertEquals("Sobota", days[0].name)
    assertEquals("Nedelja", days[1].name)
    assertTrue(days[0].data.isNotEmpty())
  }

  @Test
  fun parseHoursReturnsEightTimeColumns() {
    val hours = proVreme.parseHours(fixtureDocument("day.html"))

    assertEquals(8, hours.size)
    assertEquals("23h - 02h", hours[0].name)
    assertTrue(hours[0].data.isNotEmpty())
  }
}
