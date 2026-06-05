package eu.slickbot.scrape.utils.extension

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.jsoup.nodes.Document
import java.io.IOException

private const val MAX_ATTEMPTS = 3
private const val RETRY_DELAY_MS = 500L

fun OkHttpClient.getResponseDocument(url: String): Document {
  return getResponse(url) { it.toDocument() }
}

fun OkHttpClient.getResponseString(url: String): String {
  return getResponse(url) { it.body.string() }
}

private inline fun <T> OkHttpClient.getResponse(url: String, read: (Response) -> T): T {
  val request = Request.Builder().url(url).build()
  var lastError: IOException? = null
  for (attempt in 1..MAX_ATTEMPTS) {
    try {
      newCall(request).execute().use { response ->
        if (!response.isSuccessful) {
          throw IOException("HTTP ${response.code} for ${response.request.url}")
        }
        return read(response)
      }
    } catch (e: IOException) {
      lastError = e
      if (attempt < MAX_ATTEMPTS) {
        Thread.sleep(RETRY_DELAY_MS)
      }
    }
  }
  throw lastError!!
}
