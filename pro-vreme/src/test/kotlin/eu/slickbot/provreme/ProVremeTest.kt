package eu.slickbot.provreme

import eu.slickbot.provreme.model.ProCity
import eu.slickbot.provreme.model.ProDay
import eu.slickbot.provreme.model.ProHours
import okhttp3.OkHttpClient
import org.junit.Assert
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class ProVremeTest {

    companion object {
        private const val USER_AGENT = "Mozilla/5.0"
        private lateinit var cities: List<ProCity>
        private lateinit var days: List<ProDay>
        private lateinit var hours: List<ProHours>
    }

    private lateinit var scraper: ProVreme

    @Before
    fun setup() {
        val okHttp = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .header("User-Agent", USER_AGENT)
                    .build()
                chain.proceed(newRequest)
            }
            .build()
        scraper = ProVreme(okHttp)
    }

    @Test
    fun test_0_cities() {
        cities = scraper.getCities().apply {
            forEach { println(it) }
            Assert.assertNotEquals(size, 0)
        }
    }

    @Test
    fun test_1_days() {
        days = scraper.getDaysFor(cities[30].id).apply {
            forEach { println(it) }
            Assert.assertNotEquals(size, 0)
        }
    }

    @Test
    fun test_2_hours() {
        hours = scraper.getHoursFor(cities.first().id, days[0].id).apply {
            forEach { println(it) }
            Assert.assertNotEquals(size, 0)
        }
    }

}
