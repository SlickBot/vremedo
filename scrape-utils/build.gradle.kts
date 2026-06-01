plugins {
  alias(libs.plugins.android.library)
}

android {
  namespace = "eu.slickbot.scrape.utils"

  defaultConfig {
    minSdk = 26
    compileSdk = 37

    consumerProguardFiles("consumer-rules.pro")
  }
}

dependencies {
  // Network / scraping
  api(libs.jsoup)
  api(libs.okhttp)

  // Datetime
  api(libs.kotlinx.datetime)
}
