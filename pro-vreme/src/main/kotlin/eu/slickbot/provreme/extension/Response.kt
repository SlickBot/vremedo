package eu.slickbot.provreme.extension

import okhttp3.Response
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

internal fun Response.toDocument(): Document {
    return Jsoup.parse(requireNotNull(body).string())
}
