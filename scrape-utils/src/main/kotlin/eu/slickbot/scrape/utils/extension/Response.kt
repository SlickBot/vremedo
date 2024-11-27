package eu.slickbot.scrape.utils.extension

import okhttp3.Response
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

fun Response.toDocument(): Document {
  return Jsoup.parse(requireNotNull(body).string())
}
