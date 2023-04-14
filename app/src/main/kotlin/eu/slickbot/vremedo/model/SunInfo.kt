package eu.slickbot.vremedo.model

import kotlinx.datetime.LocalDateTime

data class SunInfo(
    val sunrise: LocalDateTime,
    val sunset: LocalDateTime,
)
