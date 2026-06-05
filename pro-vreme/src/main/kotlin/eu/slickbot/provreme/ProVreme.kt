package eu.slickbot.provreme

import eu.slickbot.provreme.Constants.API_BASE_URL
import eu.slickbot.provreme.Constants.API_DAY_URL
import eu.slickbot.provreme.Constants.API_INDEX_URL
import eu.slickbot.provreme.Constants.API_WEEK_URL
import eu.slickbot.provreme.model.Column
import eu.slickbot.provreme.model.ProCity
import eu.slickbot.provreme.model.ProData
import eu.slickbot.provreme.model.ProDay
import eu.slickbot.provreme.model.ProHours
import eu.slickbot.scrape.utils.extension.getResponseDocument
import okhttp3.OkHttpClient
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class ProVreme(private val client: OkHttpClient) {

  fun getCities(): List<ProCity> {
    return parseCities(client.getResponseDocument(API_INDEX_URL))
  }

  internal fun parseCities(document: Document): List<ProCity> {
    return document.select("#profkoKraj > option")
      .filterFillers()
      .map { ProCity(it.`val`().toInt(), it.text()) }
      .distinct()
  }

  fun getDaysFor(cityId: Int): List<ProDay> {
    return parseDays(client.getResponseDocument("$API_WEEK_URL&m=$cityId"))
  }

  internal fun parseDays(document: Document): List<ProDay> {
    return document.selectRows().parseColumns(::toDayColumn).toDays()
  }

  fun getHoursFor(cityId: Int, dayId: Int): List<ProHours> {
    return parseHours(client.getResponseDocument("$API_DAY_URL&m=$cityId&d=$dayId"))
  }

  internal fun parseHours(document: Document): List<ProHours> {
    return document.selectRows().parseColumns(::toHourColumn).toHours()
  }

  private fun Elements.filterFillers(): List<Element> {
    return filter {
      try {
        require("------" !in it.text())
        require("Izberi" !in it.text())
        require(it.`val`().isNotBlank())
        require(it.text().isNotBlank())
        true
      } catch (e: Exception) {
        false
      }
    }
  }

  private fun Document.selectRows(): Elements {
    val table = select("table#numwrf").first()
      ?: throw IllegalStateException("Missing 'table#numwrf' (page title: '${title()}')")
    return table.select("tr")
  }

  private inline fun Elements.parseColumns(toColumn: (Element, String, List<ProData>) -> Column): List<Column> {
    val elements = toList()
    val list = mutableListOf<Column>()
    val lastIndex = get(0).select("td").lastIndex

    for (i in 1..lastIndex) {
      val titleElement = get(0).select("td")[i]
      val imageElement = get(1).select("td")[i]

      val relativeUrl = imageElement.select("img").first()!!.attr("src")
      val iconUrl = "$API_BASE_URL/$relativeUrl"

      val dataList = elements.subList(2, size).mapNotNull { element ->
        if (element.className() == "vmesnik")
          return@mapNotNull null

        val cols = element.select("td")
        val titleCol = cols[0]
        val currentCol = cols[i]

        val relativeImageUrl = currentCol.select("img").first()?.attr("src")
        val imageUrl = relativeImageUrl?.let { "$API_BASE_URL/$it" }

        ProData(titleCol.text(), currentCol.text(), imageUrl)
      }

      list += toColumn(titleElement, iconUrl, dataList)
    }
    return list.toList()
  }

  private fun toDayColumn(titleElement: Element, iconUrl: String, data: List<ProData>): Column {
    val link = titleElement.select("a").first()!!
    val id = link.attr("href").split("=").last().toInt()
    val text = link.text()
    return Column(id, text, iconUrl, data)
  }

  private fun toHourColumn(titleElement: Element, iconUrl: String, data: List<ProData>): Column {
    val text = titleElement.text()
    return Column(null, text, iconUrl, data)
  }

  private fun List<Column>.toDays(): List<ProDay> {
    return mapNotNull { (id, title, iconUrl, dataList) ->
      id?.let { ProDay(it, title, iconUrl, dataList) }
    }
  }

  private fun List<Column>.toHours(): List<ProHours> {
    return map { (_, title, iconUrl, dataList) ->
      ProHours(title, iconUrl, dataList)
    }
  }

}
