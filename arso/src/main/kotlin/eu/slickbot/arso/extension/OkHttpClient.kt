package eu.slickbot.arso.extension

import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.nodes.Document

internal fun OkHttpClient.getResponseDocument(url: String): Document {
    val request = Request.Builder().url(url).build()
    val response = newCall(request).execute()
    return response.toDocument()
}

internal fun OkHttpClient.getResponseString(url: String): String {
    val request = Request.Builder().url(url).build()
    val response = newCall(request).execute()
    return response.body!!.string()
}
