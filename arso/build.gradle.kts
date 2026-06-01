plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.ksp)
}

android {
  namespace = "eu.slickbot.arso"

  defaultConfig {
    minSdk = 26
    compileSdk = 37

    consumerProguardFiles("consumer-rules.pro")
  }
}

tasks.withType<Test>().configureEach {
  useJUnit {
    if (project.hasProperty("liveTests")) {
      includeCategories("eu.slickbot.scrape.utils.LiveNetwork")
    } else {
      excludeCategories("eu.slickbot.scrape.utils.LiveNetwork")
    }
  }
}

dependencies {
  // Network
  api(libs.okhttp.logging.interceptor)
  api(libs.kotlinx.coroutines.core)
  implementation(libs.retrofit)
  implementation(libs.retrofit.converter.moshi)
  implementation(libs.moshi)
  ksp(libs.moshi.kotlin.codegen)
  api(project(":scrape-utils"))

  // Test
  testImplementation(libs.junit)
}
