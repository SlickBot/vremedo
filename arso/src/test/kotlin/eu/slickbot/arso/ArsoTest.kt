package eu.slickbot.arso

import eu.slickbot.arso.model.*
import eu.slickbot.scrape.utils.LiveNetwork
import eu.slickbot.scrape.utils.UserAgentInterceptor
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import org.junit.Assert
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.junit.experimental.categories.Category

@Category(LiveNetwork::class)
class ArsoTest {

  companion object {
    private const val USER_AGENT = "Mozilla/5.0"
  }

  private lateinit var arso: Arso

  @Before
  fun setup() {
    val client = OkHttpClient.Builder()
      .addInterceptor(UserAgentInterceptor(USER_AGENT))
      .addInterceptor(HttpLoggingInterceptor().apply { setLevel(Level.BODY) })
      .build()
    arso = Arso(client)
  }

  @Test
  fun test_location_info() = runBlocking {
    val info = arso.getLocationInfo("sl", "Novo mesto")
    println(info)
  }

  @Test
  fun test_radar_images() {
    val urls = arso.getRadarImageUrls(
      ArsoRadarLength.SHORT,
      ArsoRadarScope.NEIGHBOURS,
    )
    assertNotEquals(urls.size, 0)
    urls.forEach(::println)
  }

  @Test
  fun test_satellite_images() {
    val urls = arso.getSatelliteImageUrls(
      ArsoSatelliteLength.LONG,
      ArsoSatelliteScope.SLOVENIA,
    )
    assertNotEquals(urls.size, 0)
    urls.forEach(::println)
  }

  @Test
  fun test_aladin_images() {
    val urls = arso.getAladinImageUrls(
      ArsoAladinScope.ALPS_ADRIATIC,
      ArsoAladinMode.RAIN,
    )
    assertNotEquals(urls.size, 0)
    urls.forEach(::println)
  }

  @Test
  fun test_camera_data() {
    val data = arso.getCameraImageData()
    assertNotEquals(data.size, 0)
    data.forEach(::println)
  }

  @Test
  fun test_camera_images() {
    val data = arso.getCameraImageData()
    val randomCameraData = data.random()
    val images = arso.getCameraImageUrls(
      randomCameraData,
      randomCameraData.orientations.random(),
      ArsoCameraLength.LONG,
    )
    assertNotEquals(images.size, 0)
    images.forEach(::println)
  }

}
