package eu.slickbot.scrape.utils.extension

import java.text.SimpleDateFormat
import java.util.*

fun String.parseDate(
    pattern: String = "yyyy-MM-dd'T'HH:mm:ssZ",
    locale: Locale = Locale.getDefault(),
): Date {
    return SimpleDateFormat(pattern, locale).parse(this)!!
}
