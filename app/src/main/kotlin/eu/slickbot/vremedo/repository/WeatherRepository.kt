package eu.slickbot.vremedo.repository

import androidx.lifecycle.Lifecycle
import eu.slickbot.arso.Arso
import eu.slickbot.provreme.ProVreme
import eu.slickbot.provreme.model.ProCity
import eu.slickbot.vremedo.extension.localDateTimeNow
import eu.slickbot.vremedo.extension.toKotlinLocalDateTime
import eu.slickbot.vremedo.model.SunInfo
import eu.slickbot.vremedo.utils.AppLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.minutes

class WeatherRepository(
    private val lifecycle: AppLifecycle,
    private val arso: Arso,
    private val proVreme: ProVreme,
) {

    suspend fun getSunInfo(language: String, location: String): List<SunInfo> {
        return withContext(Dispatchers.IO) {
            arso.getLocationInfo(language, location)
                .forecast6h.features.first() // there is always only one element
                .properties.days.map { day ->
                    SunInfo(
                        sunrise = day.sunrise.toKotlinLocalDateTime(),
                        sunset = day.sunset.toKotlinLocalDateTime(),
                    )
                }
                .distinct()
        }
    }

    fun isNightFlow(location: String): Flow<Boolean> = flow {
        while (true) {
            if (lifecycle.isAtLeast(Lifecycle.State.STARTED)) {
                emit(isNight(location))
            }
            delay(1.minutes)
        }
    }

    private suspend fun isNight(location: String): Boolean {
        return !isDay(location)
    }

    private suspend fun isDay(location: String): Boolean {
        return withContext(Dispatchers.Default) {
            getSunInfo("sl", location).any {
                localDateTimeNow() in it.sunrise .. it.sunset
            }
        }
    }

    suspend fun getCities(): List<ProCity> {
        return withContext(Dispatchers.IO) {
            proVreme.getCities()
        }
    }

}
