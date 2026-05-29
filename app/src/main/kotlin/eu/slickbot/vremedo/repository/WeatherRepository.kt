package eu.slickbot.vremedo.repository

import android.content.SharedPreferences
import androidx.lifecycle.Lifecycle
import eu.slickbot.arso.Arso
import eu.slickbot.provreme.ProVreme
import eu.slickbot.provreme.model.ProCity
import eu.slickbot.provreme.model.ProData
import eu.slickbot.provreme.model.ProDay
import eu.slickbot.provreme.model.ProHours
import eu.slickbot.vremedo.extension.localDateNow
import eu.slickbot.vremedo.extension.localDateTimeNow
import eu.slickbot.vremedo.extension.localTimeNow
import eu.slickbot.vremedo.extension.toKotlinLocalDateTime
import eu.slickbot.vremedo.model.SunriseSunsetTime
import eu.slickbot.vremedo.model.WeatherCity
import eu.slickbot.vremedo.model.WeatherData
import eu.slickbot.vremedo.model.WeatherDay
import eu.slickbot.vremedo.model.WeatherHours
import eu.slickbot.vremedo.model.WeatherItem
import eu.slickbot.vremedo.utils.AppLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlin.time.Duration.Companion.minutes

class WeatherRepository(
  private val lifecycle: AppLifecycle,
  private val arso: Arso,
  private val proVreme: ProVreme,
  private val sharedPrefs: SharedPreferences,
) {

  // Cached sunrise/sunset is only valid for the day it was stored on; a missing
  // or different date means we must refresh from the network.
  private fun isNightCached(): Boolean? {
    val cachedDate = sharedPrefs.getString("day_date", null) ?: return null
    if (cachedDate != localDateNow().toString()) return null

    val dayStart = sharedPrefs.getString("day_sunrise", null) ?: return null
    val dayEnd = sharedPrefs.getString("day_sunset", null) ?: return null

    return localTimeNow() !in LocalTime.parse(dayStart)..LocalTime.parse(dayEnd)
  }

  private fun cacheTodaySunriseSunset(times: List<SunriseSunsetTime>) {
    val today = localDateNow()
    val todayTime = times.firstOrNull { it.sunrise.date == today } ?: return
    sharedPrefs.edit()
      .putString("day_date", today.toString())
      .putString("day_sunrise", todayTime.sunrise.time.toString())
      .putString("day_sunset", todayTime.sunset.time.toString())
      .apply()
  }

  fun isNightFlow(location: String): Flow<Boolean> = flow {
    isNightCached()?.let { emit(it) }
    while (true) {
      if (lifecycle.isAtLeast(Lifecycle.State.STARTED)) {
        emit(isNight(location))
      }
      delay(1.minutes)
    }
  }

  suspend fun getCities(): List<WeatherCity> {
    val cities = withContext(Dispatchers.IO) { proVreme.getCities() }
    return withContext(Dispatchers.Default) { cities.map { it.mapCity() } }
  }

  suspend fun getDays(cityId: Int): List<WeatherDay> {
    val days = withContext(Dispatchers.IO) { proVreme.getDaysFor(cityId) }
    return withContext(Dispatchers.Default) { days.map { it.mapDay() } }
  }

  suspend fun getHours(cityId: Int, dayId: Int): List<WeatherHours> {
    val hours = withContext(Dispatchers.IO) { proVreme.getHoursFor(cityId, dayId) }
    return withContext(Dispatchers.Default) { hours.map { it.mapHours() } }
  }

  suspend fun getWeatherItems(cityId: Int): List<WeatherItem> {
    return withContext(Dispatchers.Default) {
      val items = mutableListOf<WeatherItem>()

      val days = getDays(cityId)
      val dayName = getTodayDayName()

      // find today
      val today = days.firstOrNull { it.name == dayName }
      val firstDay = today ?: days.first()
      val firstDayIdx = days.indexOf(firstDay)

      for ((dayIdx, day) in days.withIndex()) {
        val dayHours = getHours(cityId, day.id)
        val daysDiff = dayIdx - firstDayIdx
        val dayDate = localDateNow() + DatePeriod(days = daysDiff)

        for ((hourSpanIdx, hourSpan) in dayHours.withIndex()) {
          val hoursLength = (hourSpan.endHour - hourSpan.startHour).let {
            if (it < 0) 24 + it else it
          }
          for (hourIdx in 0 until hoursLength) {
            val hour = (hourSpan.startHour + hourIdx) % 24

//            if (items.lastOrNull()?.dateTime?.hour == hour)
//                continue

            items += if (hourSpanIdx == 0 && hour > hourSpan.endHour) {
              WeatherItem(
                LocalDateTime(dayDate - DatePeriod(days = 1), LocalTime(hour, 0)),
                days.getOrNull(dayIdx - 1) ?: continue,
                hourSpan,
              )
            } else if (hourSpanIdx == dayHours.lastIndex && hour < hourSpan.startHour) {
              WeatherItem(
                LocalDateTime(dayDate + DatePeriod(days = 1), LocalTime(hour, 0)),
                days.getOrNull(dayIdx + 1) ?: continue,
                hourSpan,
              )
            } else {
              WeatherItem(
                LocalDateTime(dayDate, LocalTime(hour, 0)),
                day,
                hourSpan,
              )
            }
          }
        }
      }

      items
    }
  }

  // Uses the cached value while it is still fresh for today; only hits the
  // network (at most once per day) when the cache is missing or stale.
  private suspend fun isNight(location: String): Boolean {
    isNightCached()?.let { return it }

    val times = getSunriseSunsetTime("sl", location)
    isNightCached()?.let { return it }

    // Fallback: today's entry was missing from the response, compute directly.
    val now = localDateTimeNow()
    return !times.any { now in it.sunrise..it.sunset }
  }

  private suspend fun getSunriseSunsetTime(
    language: String,
    location: String,
  ): List<SunriseSunsetTime> {
    return withContext(Dispatchers.IO) {
      val times = arso.getLocationInfo(language, location)
        .forecast6h.features.first() // there is always only one element
        .properties.days.map { day ->
          SunriseSunsetTime(
            sunrise = day.sunrise.toKotlinLocalDateTime(),
            sunset = day.sunset.toKotlinLocalDateTime(),
          )
        }
        .distinct()
      cacheTodaySunriseSunset(times)
      times
    }
  }

  private suspend fun ProCity.mapCity(): WeatherCity {
    return withContext(Dispatchers.Default) {
      WeatherCity(
        id = id,
        name = name,
      )
    }
  }

  private suspend fun ProDay.mapDay(): WeatherDay {
    return withContext(Dispatchers.Default) {
      WeatherDay(
        id = id,
        name = name,
        iconUrl = iconUrl,
        dataList = data.map { it.mapData() },
      )
    }
  }

  private suspend fun ProHours.mapHours(): WeatherHours {
    return withContext(Dispatchers.Default) {
      WeatherHours(
        name = name,
        iconUrl = iconUrl,
        dataList = data.map { it.mapData() },
      )
    }
  }

  private suspend fun ProData.mapData(): WeatherData {
    return withContext(Dispatchers.Default) {
      WeatherData(
        title = title,
        text = text,
        imageUrl = imageUrl,
      )
    }
  }

  private fun getTodayDayName(): String {
    return when (localDateNow().dayOfWeek.value) {
      1 -> "Ponedeljek"
      2 -> "Torek"
      3 -> "Sreda"
      4 -> "Četrtek"
      5 -> "Petek"
      6 -> "Sobota"
      7 -> "Nedelja"
      else -> throw IllegalStateException("Unexpected dayOfWeek")
    }
  }

}
