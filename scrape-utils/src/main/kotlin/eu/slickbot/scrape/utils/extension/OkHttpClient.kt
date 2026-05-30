package eu.slickbot.scrape.utils.extension

import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.nodes.Document

fun OkHttpClient.getResponseDocument(url: String): Document {
  val request = Request.Builder().url(url).build()
  val response = newCall(request).execute()
  return response.toDocument()
}

fun OkHttpClient.getResponseString(url: String): String {
  val request = Request.Builder().url(url).build()
  val response = newCall(request).execute()
  return response.body.string()
}
