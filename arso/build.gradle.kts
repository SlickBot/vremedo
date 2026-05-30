import com.android.build.api.dsl.LibraryExtension

plugins {
  alias(libs.plugins.android.library)
}

configure<LibraryExtension> {
  namespace = "eu.slickbot.arso"

  defaultConfig {
    minSdk = 26
    compileSdk = 37

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
  implementation(libs.retrofit.converter.gson)
  api(project(":scrape-utils"))

  // Test
  testImplementation(libs.junit)
}
