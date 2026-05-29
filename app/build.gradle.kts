import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.variant.impl.VariantOutputImpl
import java.util.Properties

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.compose)
  id("maven-publish")
}

val localProps = Properties().apply {
  val f = rootProject.file("local.properties")
  if (f.exists()) f.inputStream().use { load(it) }
}

val appVersionName = "1.1"
val appVersionCode = 1

configure<ApplicationExtension> {
  namespace = "eu.slickbot.vremedo"

  defaultConfig {
    applicationId = "eu.slickbot.vremedo"

    minSdk = 26
    targetSdk = 37
    compileSdk = 37

    versionCode = appVersionCode
    versionName = appVersionName

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    vectorDrawables {
      useSupportLibrary = true
    }
  }

  signingConfigs {
    create("release") {
      storeFile = file(localProps.getProperty("keystore.storeFile") ?: "")
      storePassword = localProps.getProperty("keystore.storePassword") ?: ""
      keyAlias = localProps.getProperty("keystore.keyAlias") ?: ""
      keyPassword = localProps.getProperty("keystore.keyPassword") ?: ""
    }
  }

  buildTypes {
    release {
      signingConfig = signingConfigs.getByName("release")
      isMinifyEnabled = false
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
      )
      applicationIdSuffix = ".release"
      resValue("string", "app_name", "Vremedo")
    }
    debug {
      applicationIdSuffix = ".debug"
      resValue("string", "app_name", "Vremedo🐛")
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }

  buildFeatures {
    compose = true
    buildConfig = true
    resValues = true
  }

  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
}

androidComponents {
  onVariants { variant ->
    variant.outputs.forEach { output ->
      (output as? VariantOutputImpl)?.outputFileName?.set(
        "vremedo-$appVersionName-${variant.name}.apk"
      )
    }
  }
}

dependencies {
  // Modules
  implementation(project(":arso"))
  implementation(project(":pro-vreme"))

  // AndroidX
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.core.splashscreen)
  implementation(libs.androidx.constraintlayout.compose)

  // Compose
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.ui.tooling.preview)
  implementation(libs.androidx.compose.animation)
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.compose.material.icons.extended)
  implementation(libs.androidx.navigation.compose)
  debugImplementation(libs.androidx.compose.ui.tooling)
  debugImplementation(libs.androidx.compose.ui.test.manifest)

  // DI
  implementation(libs.koin.androidx.compose)

  // Image loading
  implementation(libs.coil.compose)

  // Logging
  implementation(libs.timber)

  // Test
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(libs.androidx.compose.ui.test.junit4)
}

publishing {
  publications {
    create<MavenPublication>("releaseApk") {
      groupId = "eu.slickbot"
      artifactId = "vremedo-release"
      version = appVersionName
      artifact(layout.buildDirectory.file("outputs/apk/release/vremedo-$appVersionName-release.apk")) {
        extension = "apk"
      }
    }
    create<MavenPublication>("debugApk") {
      groupId = "eu.slickbot"
      artifactId = "vremedo-debug"
      version = appVersionName
      artifact(layout.buildDirectory.file("outputs/apk/debug/vremedo-$appVersionName-debug.apk")) {
        extension = "apk"
      }
    }
  }
  repositories {
    maven {
      url = uri("http://brane:8081/repository/apk-releases/")
      isAllowInsecureProtocol = true
      credentials {
        username = localProps.getProperty("nexus.username") ?: ""
        password = localProps.getProperty("nexus.password") ?: ""
      }
    }
  }
}

// Ensure each APK publication assembles its variant first (lazy, name-based wiring).
tasks.matching { it.name == "publishReleaseApkPublicationToMavenRepository" }
  .configureEach { dependsOn("assembleRelease") }
tasks.matching { it.name == "publishDebugApkPublicationToMavenRepository" }
  .configureEach { dependsOn("assembleDebug") }

tasks.register("assembleAll") {
  group = "build"
  description = "Builds debug and release APKs and copies them to releases/{versionName}"

  dependsOn("assembleDebug", "assembleRelease")

  doLast {
    val outputDir = layout.buildDirectory.dir("outputs/apk").get().asFile
    val releaseDir = file("releases/$appVersionName")
    releaseDir.mkdirs()

    fileTree(outputDir) { include("**/*.apk") }.files.forEach { apk ->
      copy {
        from(apk)
        into(releaseDir)
      }
    }

    println("Copied APKs to releases/$appVersionName")
  }
}

tasks.register("publishAll") {
  group = "build"
  description = "Publishes all build types to nexus"

  dependsOn(
    "publishDebugApkPublicationToMavenRepository",
    "publishReleaseApkPublicationToMavenRepository"
  )

  doLast {
    println("Published APKs to Nexus")
  }
}
