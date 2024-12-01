package eu.slickbot.vremedo

import android.content.Context
import android.content.SharedPreferences
import eu.slickbot.arso.Arso
import eu.slickbot.provreme.ProVreme
import eu.slickbot.vremedo.extension.runIf
import eu.slickbot.vremedo.repository.WeatherRepository
import eu.slickbot.vremedo.screen.splash.SplashViewModel
import eu.slickbot.vremedo.screen.weather.WeatherViewModel
import eu.slickbot.vremedo.utils.AppLifecycle
import eu.slickbot.vremedo.utils.AppNavigation
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
  viewModelModules()
  repositoryModules()
  utilityModules()
}

fun Module.viewModelModules() {
  viewModelOf(::SplashViewModel)
  viewModelOf(::WeatherViewModel)
}

fun Module.repositoryModules() {
  singleOf(::WeatherRepository)
}

fun Module.utilityModules() {
  singleOf(::defaultSharedPrefs)
  singleOf(::defaultHttpClient)

  singleOf(::AppLifecycle)
  singleOf(::AppNavigation)

  singleOf(::Arso)
  singleOf(::ProVreme)
}

private fun defaultSharedPrefs(context: Context): SharedPreferences {
  return context.getSharedPreferences("prefs-vremedo", Context.MODE_PRIVATE)
}

private fun defaultHttpClient(): OkHttpClient {
  return OkHttpClient.Builder()
    .runIf(BuildConfig.DEBUG) {
      addInterceptor(
        HttpLoggingInterceptor().apply { setLevel(Level.BODY) }
      )
    }
    .build()
}
