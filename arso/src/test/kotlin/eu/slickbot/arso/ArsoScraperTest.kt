package eu.slickbot.arso

import eu.slickbot.arso.model.*
import okhttp3.OkHttpClient
import org.junit.Assert
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class ArsoScraperTest {

    companion object {
//        private lateinit var temperatures: List<ArsoTemperature>
//        private lateinit var warnings: List<ArsoAlarm>
    }

    private lateinit var arso: Arso

    @Before
    fun setup() {
        val client = OkHttpClient()
        arso = Arso(client)
    }

    @Test
    fun test_radar_images() {
        val urls = arso.getRadarImageUrls(
            ArsoRadarLength.SHORT,
            ArsoRadarScope.NEIGHBOURS,
        )
        Assert.assertNotEquals(urls.size, 0)
        urls.forEach(::println)
    }

    @Test
    fun test_satellite_images() {
        val urls = arso.getSatelliteImageUrls(
            ArsoSatelliteLength.LONG,
            ArsoSatelliteScope.SLOVENIA,
        )
        Assert.assertNotEquals(urls.size, 0)
        urls.forEach(::println)
    }

    @Test
    fun test_aladin_images() {
        val urls = arso.getAladinImageUrls(
            ArsoAladinScope.ALPS_ADRIATIC,
            ArsoAladinMode.RAIN,
        )
        Assert.assertNotEquals(urls.size, 0)
        urls.forEach(::println)
    }

    @Test
    fun test_camera_data() {
        val data = arso.getCameraImageData()
        Assert.assertNotEquals(data.size, 0)
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
        Assert.assertNotEquals(images.size, 0)
        images.forEach(::println)
    }

}
