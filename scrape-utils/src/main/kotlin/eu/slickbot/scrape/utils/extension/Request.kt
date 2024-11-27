package eu.slickbot.scrape.utils.extension

import okhttp3.Response

fun Response.getHeaderValue(header: String, ignoreCase: Boolean = false): String? {
  return headers.names()
    .find { it.equals(header, ignoreCase = ignoreCase) }
    ?.let { headers[it] }
}
