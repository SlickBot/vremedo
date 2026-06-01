package eu.slickbot.arso.model

enum class ArsoLanguage {
  SLOVENIAN, ENGLISH,
}

enum class ArsoRadarLength(val displayName: String) {
  LONG("Long"),
  SHORT("Short"),
  LATEST("Latest"),
}

enum class ArsoRadarScope(val displayName: String) {
  SLOVENIA("Slovenia"),
  NEIGHBOURS("Neighbours"),
}

enum class ArsoSatelliteLength(val displayName: String) {
  LONG("Long"),
  LATEST("Latest"),
}

enum class ArsoSatelliteScope(val displayName: String) {
  SLOVENIA("Slovenia"),
  EUROPE("Europe"),
}

enum class ArsoAladinScope(val displayName: String) {
  SLOVENIA("Slovenia"),
  ALPS_ADRIATIC("Alps-Adriatic"),
}

enum class ArsoAladinMode(val displayName: String) {
  RAIN("Rain"),
  TEMPERATURE("Temperature"),
  WIND_FLOOR("Surface wind"),
  WIND_700M("Wind 700m"),
  WIND_1500M("Wind 1500m"),
}

enum class ArsoCameraLength(val displayName: String) {
  LONG("Long"),
  SHORT("Short"),
  LATEST("Latest"),
}

enum class ArsoCameraOrientation(val code: String, val displayName: String) {
  N("n", "N"),
  NE("ne", "NE"),
  E("e", "E"),
  SE("se", "SE"),
  S("s", "S"),
  SW("sw", "SW"),
  W("w", "W"),
  NW("nw", "NW"),
  ;

  companion object {
    fun fromCode(code: String): ArsoCameraOrientation {
      return entries.first { it.code.equals(code, ignoreCase = true) }
    }
  }
}

enum class ArsoAlertScope {
  SLOVENIA_CENTRAL,
  SLOVENIA_NW,
  SLOVENIA_NE,
  SLOVENIA_SW,
  SLOVENIA_SE,
}

enum class ArsoAudioBitrate {
  MEDIUM, HIGH,
}
