package eu.slickbot.scrape.utils.extension

import java.util.regex.Matcher

fun Matcher.findAllGroups(group: Int): List<String> {
  val list = mutableListOf<String>()
  while (find()) {
    val url = group(group)
    if (url != null) {
      list += url
    }
  }
  return list
}

fun Matcher.findAll(): List<List<String>> {
  val list = mutableListOf<List<String>>()
  while (find()) {
    list += (0..groupCount()).mapNotNull { group(it) }
  }
  return list
}

