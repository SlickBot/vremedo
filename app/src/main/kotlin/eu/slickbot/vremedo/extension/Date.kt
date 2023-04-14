package eu.slickbot.vremedo.extension

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.OffsetDateTime
import java.time.LocalDateTime as JavaLocalDateTime

fun String.toKotlinLocalDateTime(): LocalDateTime {
    return OffsetDateTime.parse(this)
        .toLocalDateTime()
        .toKotlinLocalDateTime()
}

fun localDateTimeNow(): LocalDateTime {
    return JavaLocalDateTime.now()
        .toKotlinLocalDateTime()
}
