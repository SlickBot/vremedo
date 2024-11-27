package eu.slickbot.vremedo.extension

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.datetime.toKotlinLocalTime
import java.time.OffsetDateTime
import java.time.LocalDate as JavaLocalDate
import java.time.LocalDateTime as JavaLocalDateTime
import java.time.LocalTime as JavaLocalTime

fun String.toKotlinLocalDateTime(): LocalDateTime {
    return OffsetDateTime.parse(this)
        .toLocalDateTime()
        .toKotlinLocalDateTime()
}

fun localDateTimeNow(): LocalDateTime {
    return JavaLocalDateTime.now()
        .toKotlinLocalDateTime()
}

fun localTimeNow(): LocalTime {
    return JavaLocalTime.now()
        .toKotlinLocalTime()
}

fun localDateNow(): LocalDate {
    return JavaLocalDate.now()
        .toKotlinLocalDate()
}
