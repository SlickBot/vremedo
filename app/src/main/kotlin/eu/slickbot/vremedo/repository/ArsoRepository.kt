package eu.slickbot.vremedo.repository

import eu.slickbot.arso.Arso
import eu.slickbot.arso.model.ArsoAladinMode
import eu.slickbot.arso.model.ArsoAladinScope
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

}
