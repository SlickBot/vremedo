package eu.slickbot.vremedo.model

import kotlinx.datetime.LocalDateTime

data class WeatherItem(
    val dateTime: LocalDateTime,
    val day: WeatherDay,
    val hours: WeatherHours,
)
