package eu.slickbot.arso.model

enum class ArsoLanguage {
  SLOVENIAN, ENGLISH,
}

enum class ArsoRadarLength {
  LONG, SHORT, LATEST,
}

enum class ArsoRadarScope {
  SLOVENIA, NEIGHBOURS,
}

enum class ArsoSatelliteLength {
  LONG, LATEST,
}

enum class ArsoSatelliteScope {
  SLOVENIA, EUROPE,
}

enum class ArsoAladinScope {
  SLOVENIA, ALPS_ADRIATIC,
}

enum class ArsoAladinMode {
  RAIN, TEMPERATURE, WIND_FLOOR, WIND_700M, WIND_1500M,
}

enum class ArsoCameraLength {
  LONG, SHORT, LATEST,
}

enum class ArsoCameraOrientation {
  N, NE, E, SE, S, SW, W, NW,
}

enum class ArsoWeatherScope {
  SLOVENIA, EUROPE,
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
