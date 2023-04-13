package eu.slickbot.vremedo

import eu.slickbot.vremedo.screen.splash.SplashViewModel
import eu.slickbot.vremedo.screen.weather.WeatherViewModel
import eu.slickbot.vremedo.utils.AppLifecycle
import eu.slickbot.vremedo.utils.AppNavigation
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

}

fun Module.utilityModules() {
    singleOf(::AppLifecycle)
    singleOf(::AppNavigation)
}
