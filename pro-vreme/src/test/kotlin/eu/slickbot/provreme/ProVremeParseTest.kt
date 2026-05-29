package eu.slickbot.provreme

import eu.slickbot.provreme.model.ProCity
import okhttp3.OkHttpClient
import org.jsoup.Jsoup
import org.junit.Assert.assertEquals
import org.junit.Test

class ProVremeParseTest {

  // parseCities does not touch the network, so a bare client is fine.
  private val proVreme = ProVreme(OkHttpClient())

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
}
