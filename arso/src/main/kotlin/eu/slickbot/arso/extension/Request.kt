package eu.slickbot.arso.extension

import okhttp3.Response

fun Response.findHeader(header: String, ignoreCase: Boolean = false): String? {
    for (name in headers.names()) {
        if (name.equals(header, ignoreCase = ignoreCase))
            return headers[name]
    }
    return null
}
