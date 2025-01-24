package eu.slickbot.vremedo.repository

import eu.slickbot.arso.Arso
import eu.slickbot.arso.model.ArsoAladinMode
import eu.slickbot.arso.model.ArsoAladinScope
import eu.slickbot.arso.model.ArsoCameraData
import eu.slickbot.arso.model.ArsoCameraLength
import eu.slickbot.arso.model.ArsoCameraOrientation
import eu.slickbot.arso.model.ArsoRadarLength
import eu.slickbot.arso.model.ArsoRadarScope
import eu.slickbot.arso.model.ArsoSatelliteLength
import eu.slickbot.arso.model.ArsoSatelliteScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ArsoRepository(
  private val arso: Arso,
) {

  suspend fun getAladinImages(
    scope: ArsoAladinScope,
    mode: ArsoAladinMode,
  ): List<String> {
    return withContext(Dispatchers.IO) {
      arso.getAladinImageUrls(scope, mode)
    }
  }

  suspend fun getRadarImages(
    length: ArsoRadarLength,
    scope: ArsoRadarScope,
  ): List<String> {
    return withContext(Dispatchers.IO) {
      arso.getRadarImageUrls(length, scope)
    }
  }

  suspend fun getSatelliteImages(
    length: ArsoSatelliteLength,
    scope: ArsoSatelliteScope,
  ): List<String> {
    return withContext(Dispatchers.IO) {
      arso.getSatelliteImageUrls(length, scope)
    }
  }

  suspend fun getCameraData(): List<ArsoCameraData> {
    return withContext(Dispatchers.IO) {
      arso.getCameraImageData()
    }
  }

  suspend fun getCamerasImages(
    data: ArsoCameraData,
    orientation: ArsoCameraOrientation,
    length: ArsoCameraLength,
  ): List<String> {
    return withContext(Dispatchers.IO) {
      arso.getCameraImageUrls(data, orientation, length)
    }
  }

}
