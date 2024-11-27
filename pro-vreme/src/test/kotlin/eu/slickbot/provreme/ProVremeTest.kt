package eu.slickbot.provreme

import eu.slickbot.provreme.model.ProCity
import eu.slickbot.provreme.model.ProDay
import eu.slickbot.scrape.utils.UserAgentInterceptor
import okhttp3.OkHttpClient
import org.junit.Assert
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test

class ProVremeTest {

  companion object {
    private const val USER_AGENT = "Mozilla/5.0"

    private val TEST_CITY = ProCity(2, "Ljubljana")
    private val TEST_DAY = ProDay(2, "Monday", "", emptyList())
  }

  private lateinit var scraper: ProVreme

  @Before
  fun setup() {
    val okHttp = OkHttpClient.Builder()
      .addInterceptor(UserAgentInterceptor(USER_AGENT))
      .build()
    scraper = ProVreme(okHttp)
  }

  @Test
  fun getCities() {
    val cities = scraper.getCities()
    assertNotEquals(cities.size, 0)
    cities.forEach(::println)
  }

  @Test
  fun getDays() {
    val days = scraper.getDaysFor(TEST_CITY.id)
    assertNotEquals(days.size, 0)
    days.forEach(::println)
  }

  @Test
  fun getHours() {
    val hours = scraper.getHoursFor(TEST_CITY.id, TEST_DAY.id)
    assertNotEquals(hours.size, 0)
    hours.forEach(::println)
  }

}
