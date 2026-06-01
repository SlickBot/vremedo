plugins {
  alias(libs.plugins.android.library)
}

android {
  namespace = "eu.slickbot.provreme"

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
  api(project(":scrape-utils"))

  // Test
  testImplementation(libs.junit)
}
