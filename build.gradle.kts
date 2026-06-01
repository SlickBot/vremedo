import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.android.library) apply false
  alias(libs.plugins.kotlin.compose) apply false
}

subprojects {
  pluginManager.withPlugin("com.android.base") {
    extensions.configure<KotlinAndroidProjectExtension> {
      jvmToolchain(21)
    }
  }

  tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
      freeCompilerArgs.add("-Xannotation-default-target=param-property")
    }
  }
}
