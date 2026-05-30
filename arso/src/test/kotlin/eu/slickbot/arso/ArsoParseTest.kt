package eu.slickbot.arso

import eu.slickbot.arso.model.ArsoAudioBitrate
import eu.slickbot.arso.model.ArsoLanguage
import okhttp3.OkHttpClient
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.time.Instant

class ArsoParseTest {

  private val arso = Arso(OkHttpClient())

  private fun fixture(name: String): String {
    return javaClass.getResource("/fixtures/$name")!!.readText()
  }

  @Test
  fun parsesSatelliteImageUrls() {
    val urls = arso.parseSatelliteImageUrls(fixture("satellite_hrv_si_long.xml"))
    assertEquals(15, urls.size)
    assertEquals(
      "https://meteo.arso.gov.si/uploads/probase/www/observ/satellite/msg_20260530-0400_hrv_si.jpg",
      urls.first(),
    )
    assertEquals(
      "https://meteo.arso.gov.si/uploads/probase/www/observ/satellite/msg_20260530-1800_hrv_si.jpg",
      urls.last(),
    )
  }

  @Test
  fun parsesRadarImageUrls() {
    val urls = arso.parseRadarImageUrls(fixture("radar_si-neighbours_short.xml"))
    assertEquals(36, urls.size)
    assertEquals(
      "https://meteo.arso.gov.si/uploads/probase/www/observ/radar/si0_20260530-1855_zm_si-neighbours.jpg",
      urls.first(),
    )
  }

  @Test
  fun parsesAladinImageUrls() {
    val urls = arso.parseAladinImageUrls(fixture("aladin_tcc-rr_alps-adriatic.xml"))
    assertEquals(24, urls.size)
    assertEquals(
      "https://meteo.arso.gov.si/uploads/probase/www/model/aladin/field/as_20260530-1200_tcc-rr_alps-adriatic_003.png",
      urls.first(),
    )
  }

  @Test
  fun parsesCameraImageUrls() {
    val data = eu.slickbot.arso.model.ArsoCameraData(
      "KUM_", "", "", "", "", emptyList(), "", emptyList(),
    )
    val urls = arso.parseCameraImageUrls(fixture("camera_timeline_kum_nw_short.xml"), data)
    assertEquals(13, urls.size)
    assertEquals(
      "https://meteo.arso.gov.si/uploads/probase/www/observ/webcam/KUM_dir/siwc_20260530-1850_KUM_nw.jpg",
      urls.first(),
    )
  }

  @Test
  fun parsesCameraImageDataWithPopulatedFields() {
    val data = arso.parseCameraImageData(
      fixture("camera_index.xml"),
      fixture("camera_title.xml"),
      fixture("camera_webcam.xml"),
    )

    assertTrue(data.isNotEmpty())
    data.forEach {
      assertTrue(it.id.isNotBlank())
      assertTrue(it.title.isNotBlank())
      assertTrue(it.urls.isNotEmpty())
      assertTrue(it.orientations.isNotEmpty())
    }
  }

  @Test
  fun parsesAlert() {
    val alert = arso.parseAlert(fixture("alert_slovenia_middle.xml"))

    assertEquals("2.49.0.0.705.0.SI.260530181800.0090101", alert.identifier)
    assertEquals("dezurni.prognostik@arso.gov.si", alert.sender)
    assertEquals(Instant.parse("2026-05-30T18:18:00+02:00"), alert.sent)
    assertEquals("Actual", alert.status)
    assertEquals("Alert", alert.msgType)
    assertEquals("Public", alert.scope)
    assertEquals(34, alert.info.size)

    val first = alert.info.first()
    assertEquals("sl", first.language)
    assertEquals("Met", first.category)
    assertEquals("Veter - neznatna ogroženost", first.event)
    assertEquals("Minor", first.severity)
    assertEquals(Instant.parse("2026-05-30T18:10:00+02:00"), first.effective)
    assertEquals(Instant.parse("2026-05-30T01:00:00+02:00"), first.onset)
    assertEquals(Instant.parse("2026-06-04T00:59:00+02:00"), first.expires)
    assertEquals("Slovenija / osrednja", first.area.areaDesc)
  }

  @Test
  fun returnsEmptyForContentWithoutTimeline() {
    assertEquals(emptyList<String>(), arso.parseSatelliteImageUrls("<pujs>no timeline here</pujs>"))
    assertEquals(emptyList<String>(), arso.parseAladinImageUrls(""))
  }

  @Test
  fun buildsWeatherAudioUrl() {
    assertEquals(
      "https://meteo.arso.gov.si/uploads/probase/www/observ/media/sl/observation_si_audio_mbr.mp3",
      arso.getWeatherAudioUrl(ArsoAudioBitrate.MEDIUM, ArsoLanguage.SLOVENIAN),
    )
    assertEquals(
      "https://meteo.arso.gov.si/uploads/probase/www/observ/media/sl/observation_en_audio_hbr.mp3",
      arso.getWeatherAudioUrl(ArsoAudioBitrate.HIGH, ArsoLanguage.ENGLISH),
    )
  }
}
