import com.android.build.api.dsl.LibraryExtension

plugins {
  alias(libs.plugins.android.library)
}

configure<LibraryExtension> {
  namespace = "eu.slickbot.scrape.utils"

  defaultConfig {
    minSdk = 26
    compileSdk = 37

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
      )
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  lint {
    targetSdk = 37
  }
  testOptions {
    targetSdk = 37
  }
}

dependencies {
  // Network / scraping
  api(libs.jsoup)
  api(libs.gson)
  api(libs.okhttp)

  // Datetime
  api(libs.kotlinx.datetime)
}
