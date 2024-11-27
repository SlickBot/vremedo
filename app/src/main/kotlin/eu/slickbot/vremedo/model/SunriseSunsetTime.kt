package eu.slickbot.vremedo.model

import kotlinx.datetime.LocalDateTime

data class SunriseSunsetTime(
    val sunrise: LocalDateTime,
    val sunset: LocalDateTime,
)
