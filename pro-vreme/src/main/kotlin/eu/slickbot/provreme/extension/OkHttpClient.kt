package eu.slickbot.provreme.extension

import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.nodes.Document

internal fun OkHttpClient.getResponseDocument(url: String): Document {
    val request = Request.Builder().url(url).build()
    val response = newCall(request).execute()
    return response.toDocument()
}
