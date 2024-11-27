package eu.slickbot.arso

import eu.slickbot.arso.model.*
import eu.slickbot.scrape.utils.extension.*
import kotlinx.datetime.Instant
import kotlinx.datetime.toInstant
import okhttp3.OkHttpClient
import org.xml.sax.InputSource
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.StringReader
import java.lang.Integer.min
import javax.xml.parsers.DocumentBuilderFactory

class Arso(private val client: OkHttpClient = OkHttpClient()) {

    companion object {

        private const val BASE_URL = "https://meteo.arso.gov.si"
        private const val SERVICE_URL = "$BASE_URL/met/sl/service"

        private const val BASE_DATA_URL = "$BASE_URL/uploads/probase/www/plus/timeline"
        private const val BASE_OBSERV_URL = "$BASE_URL/uploads/probase/www/observ"
        private const val BASE_MODEL_URL = "$BASE_URL/uploads/probase/www/model"

    }

    private val service = Retrofit.Builder()
        .baseUrl("https://vreme.arso.gov.si/api/1.0/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ArsoService::class.java)

    suspend fun getLocationInfo(language: String, location: String): ArsoLocationInfo {
        return service.locationInfo(language, location)
    }

    fun getCategoryGroups(): List<ArsoCategoryGroup> {
        val content = client.getResponseDocument(SERVICE_URL)
            .selectFirst("td.vsebina")

        val groups = mutableListOf<ArsoCategoryGroup>()
        var currentGroup: ArsoCategoryGroup? = null
        var currentGroupItems = mutableListOf<ArsoCategoryItem>()

        for (element in content.children()) {
            if (element.`is`("h2")) {
                if (currentGroup != null) {
                    groups += currentGroup.copy(items = currentGroupItems.toList())
                    currentGroupItems = mutableListOf()
                }
                currentGroup = ArsoCategoryGroup(element.text(), emptyList())
            }
            if (element.`is`("a")) {
                currentGroupItems += ArsoCategoryItem(element.text(), element.attr("href"))
            }
        }

        if (currentGroup != null) {
            groups += currentGroup.copy(items = currentGroupItems.toList())
        }

        return groups.toList().filter { it.items.isNotEmpty() }
    }

    fun getDataGroups(item: ArsoCategoryItem): List<ArsoDataGroup> {
        val content = client.getResponseDocument("$BASE_URL${item.link}")
            .selectFirst("div.content")
            .selectFirst("tbody")
            .select("tr")
            .filterIndexed { index, _ -> index != 0 } // skip first item
            .map {
                val children = it.children()
                println(children)
            }
        return emptyList()
    }



    // https://meteo.arso.gov.si/uploads/probase/www/plus/sl/weather/data/weather_eu.xml
    // https://meteo.arso.gov.si/uploads/probase/www/plus/sl/forecast/data/forecast_eu.xml?nocache=laeftj6e21y3osl54kh
    fun getWeather(
        scope: ArsoWeatherScope,
    ) {

    }


    // https://meteo.arso.gov.si/uploads/probase/www/plus/timeline/timeline_satellite_ir_sateu_long.xml
    // https://meteo.arso.gov.si/uploads/probase/www/plus/timeline/timeline_satellite_hrv_si_long.xml
    fun getSatelliteImageUrls(
        length: ArsoSatelliteLength,
        scope: ArsoSatelliteScope,
    ): List<String> {
        val lengthText = when (length) {
            ArsoSatelliteLength.LONG -> "long"
            ArsoSatelliteLength.LATEST -> "latest"
        }
        val scopeText = when (scope) {
            ArsoSatelliteScope.SLOVENIA -> "hrv_si"
            ArsoSatelliteScope.EUROPE -> "ir_sateu"
        }
        val url = "${BASE_DATA_URL}/timeline_satellite_${scopeText}_${lengthText}.xml"
        val response = client.getResponseString(url)
        val pattern = "\\{(.*)url:IMG\\+'(.*)'(.*)\\}".toPattern()
        val matcher = pattern.matcher(response)
        val imageUrls = matcher.findAllGroups(2)
        return imageUrls.map { "$BASE_OBSERV_URL/satellite/$it" }
    }

    // https://meteo.arso.gov.si/uploads/probase/www/plus/timeline/timeline_aladin_vm-va10m_si-neighbours.xml
    // https://meteo.arso.gov.si/uploads/probase/www/plus/timeline/timeline_aladin_vm-va10m_alps-adriatic.xml
    fun getAladinImageUrls(
        scope: ArsoAladinScope,
        mode: ArsoAladinMode,
    ): List<String> {
        val scopeText = when (scope) {
            ArsoAladinScope.SLOVENIA -> if (mode == ArsoAladinMode.WIND_FLOOR) "si" else "si-neighbours"
            ArsoAladinScope.ALPS_ADRIATIC -> "alps-adriatic"
        }
        val modeText = when (mode) {
            ArsoAladinMode.RAIN -> "tcc-rr"
            ArsoAladinMode.TEMPERATURE -> "t2m"
            ArsoAladinMode.WIND_FLOOR -> "vm-va10m"
            ArsoAladinMode.WIND_700M -> "vf925"
            ArsoAladinMode.WIND_1500M -> "r-t-vf850"
        }
        val url = "${BASE_DATA_URL}/timeline_aladin_${modeText}_${scopeText}.xml"
        val responseString = client.getResponseString(url)
        return responseString
            .matcher("\\{(.*)url:IMG\\+'(.*)',(.*)\\}".toRegex())
            .findAllGroups(2)
            .map { "$BASE_MODEL_URL/aladin/field/$it" }
    }

    // https://meteo.arso.gov.si/uploads/probase/www/plus/timeline/timeline_radar_si_short.xml
    // https://meteo.arso.gov.si/uploads/probase/www/plus/timeline/timeline_radar_si-neighbours_long.xml
    fun getRadarImageUrls(
        length: ArsoRadarLength,
        scope: ArsoRadarScope,
    ): List<String> {
        val lengthText = when (length) {
            ArsoRadarLength.SHORT -> "short"
            ArsoRadarLength.LONG -> "long"
            ArsoRadarLength.LATEST -> "latest"
        }
        val scopeText = when (scope) {
            ArsoRadarScope.SLOVENIA -> "si"
            ArsoRadarScope.NEIGHBOURS -> "si-neighbours"
        }
        val url = "${BASE_DATA_URL}/timeline_radar_${scopeText}_${lengthText}.xml"
        val response = client.getResponseString(url)
        val pattern = "\\{(.*)url:IMG\\+'(.*)'(.*)\\}".toPattern()
        val matcher = pattern.matcher(response)
        val imageUrls = matcher.findAllGroups(2)
        return imageUrls.map { "$BASE_OBSERV_URL/radar/$it" }
    }

    fun getCameraImageData(): List<ArsoCameraData> {

        /*
         * Orientation
         */

        val orientationsUrl = "$BASE_URL/uploads/meteo/app/webmet/pujs/prog/index.xml"
        val orientationsResponse = client.getResponseString(orientationsUrl)

        // ids
        // there are many 'doms=...' declarations in response
        // find the one with known values "LJUBL" and "MARIB"

        var domsText = ""
        var domsStartIdx = 0
        var domsEndIdx = 0

        while (true) {
            domsStartIdx = orientationsResponse.indexOf("doms=[", startIndex = domsEndIdx)
            if (domsStartIdx == -1)
                break
            domsEndIdx = orientationsResponse.indexOf("]", startIndex = domsStartIdx)
            if (domsEndIdx == -1)
                break

            domsText = orientationsResponse.substring(domsStartIdx, domsEndIdx + 1)

            if ("LJUBL" in domsText && "MARIB" in domsText) {
                break
            }
            if (domsEndIdx >= orientationsResponse.length) {
                break
            }
        }

        val ids = domsText.removePrefixSuffix("doms=[", "]")
            .split(",")
            .map { it.removePrefixSuffix("\"") }

        // excluded ids

        val excludedStartIdx = orientationsResponse.indexOf("excludeTimeline=[", startIndex = domsEndIdx)
        val excludedEndIdx = orientationsResponse.indexOf("]", startIndex = excludedStartIdx)
        val excludedText = orientationsResponse.substring(excludedStartIdx, excludedEndIdx+1)

        val excludedIds = excludedText
            .removePrefixSuffix("excludeTimeline=[", "]")
            .split(",")
            .map { it.removePrefixSuffix("\"") }

        // types

        val typesStartIdx = orientationsResponse.indexOf("var typeList={", startIndex = domsEndIdx)
        val typesEndIdx = orientationsResponse.indexOf("}", startIndex = typesStartIdx)
        val typesText = orientationsResponse.substring(typesStartIdx, typesEndIdx + 1)

        val directionsList = typesText.removePrefix("var typeList={").removeSuffix("}")
            .let { string ->
                string.indicesOf(":[").map { idx ->
                    val startIdx = idx + 2
                    val endIdx = string.indexOf("]", startIndex = startIdx)
                    string.substring(startIdx, endIdx)
                        .split(",")
                        .map { it.removePrefixSuffix("\"") }
                        .map { ArsoCameraOrientation.valueOf(it.uppercase()) }
                }
            }

        val directionsMap = mutableMapOf<String, List<ArsoCameraOrientation>>()
        for (i in 0 until min(ids.size, directionsList.size)) {
            directionsMap[ids[i]] = directionsList[i]
        }
        for (excludedId in excludedIds) {
            directionsMap.remove(excludedId)
        }

        /*
         * Titles
         */

        val titlesUrl = "$BASE_URL/uploads/meteo/app/webmet/pujs/locale/sl/title.xml"
        val titlesResponse = client.getResponseString(titlesUrl)

        val titlesMap = titlesResponse.indicesOf("NAV_")
            .associate { startIdx ->
                val middleIdx = titlesResponse.indexOf(":\"", startIndex = startIdx)
                val endIdx = titlesResponse.indexOf("\"", startIndex = middleIdx + 2)
                val key = titlesResponse.substring(startIdx, middleIdx)
                val value = titlesResponse.substring(middleIdx + 2, endIdx)
                key to value
            }

        /*
         * Cameras
         */

        val camerasUrl = "$BASE_URL/uploads/meteo/app/webmet/pujs/prog/navigator/webcam.xml"
        val camerasResponse = client.getResponseString(camerasUrl)
//        val camerasResponse = CAMERA_DATA

        val cameraList = camerasResponse
            .matcher("domains:\\[(.*)]".toRegex())
            .findAllGroups(1).first()
            .matcher("domains:\\[(.*)]".toRegex())
            .findAllGroups(1).first()
            .removePrefixSuffix("{", "}")
            .split("},{")
            .map {
                it.matcher("id:\"(.*)\",meteosiid:\"(.*)\",crs:(.*),crsArg:\\{(.*)\\},icon:\"(.*)\",url:\\[(.*)],text:(.*)".toRegex())
                    .findAll()
                    .first()
            }
//            .map { it + directionsMap[it[2]].toString() }

        // https://meteo.arso.gov.si/uploads/probase/www/plus/timeline/timeline_webcam_KUM_nw_short.xml
        // https://meteo.arso.gov.si/uploads/probase/www/plus/timeline/timeline_webcam_KUM_se_short.xml

        /*
         * Together
         */

        val cameraData = cameraList.mapNotNull { data ->
            val id = data[1]
            val meteoSiId = data[2]
            val crs = data[3]
            val crsArg = data[4]
            val icon = data[5]
            val urls = data[6].split(",").map { it.removePrefixSuffix("\"") }
            val textKey = data[7].removePrefix("ACADEMA.TEXT.")
            val title = titlesMap[textKey]!!
            val directions = directionsMap[id]

            if (directions == null) {
                null
            } else {
                ArsoCameraData(id, meteoSiId, crs, crsArg, icon, urls, title, directions)
            }
        }

        return cameraData
    }

    fun getCameraImageUrls(
        data: ArsoCameraData,
        orientation: ArsoCameraOrientation,
        size: ArsoCameraLength,
    ): List<String> {
        val lengthText = when (size) {
            ArsoCameraLength.LATEST -> "latest"
            ArsoCameraLength.SHORT -> "short"
            ArsoCameraLength.LONG -> "long"
        }
        val orientationText = orientation.name.lowercase()
        val url = "$BASE_URL/uploads/probase/www/plus/timeline/timeline_webcam_${data.id}${orientationText}_${lengthText}.xml"

        return client.getResponseString(url)
            .matcher("\\{(.*)url:IMG\\+'(.*)'(.*)\\}".toRegex())
            .findAllGroups(2)
            .map { "$BASE_OBSERV_URL/webcam/${data.id}dir/$it" }
    }

    fun getWeatherAudioUrl(
        bitrate: ArsoAudioBitrate,
        language: ArsoLanguage,
    ): String {
        val bitrateText = when (bitrate) {
            ArsoAudioBitrate.MEDIUM -> "mbr"
            ArsoAudioBitrate.HIGH -> "hbr"
        }
        val languageText = when (language) {
            ArsoLanguage.SLOVENIAN -> "si"
            ArsoLanguage.ENGLISH -> "en"
        }

        return "$BASE_URL/uploads/probase/www/observ/media/sl/observation_${languageText}_audio_${bitrateText}.mp3"
    }

    // https://meteo.arso.gov.si/uploads/probase/www/warning/text/sl/warning_SLOVENIA_MIDDLE_latest_CAP.xml
    fun getAlerts(
        scope: ArsoAlertScope,
    ): ArsoAlert {

        val scopeText = when (scope) {
            ArsoAlertScope.SLOVENIA_CENTRAL -> "SLOVENIA_MIDDLE"
            ArsoAlertScope.SLOVENIA_NW -> "SLOVENIA_NORTH-WEST"
            ArsoAlertScope.SLOVENIA_NE -> "SLOVENIA_NORTH-EAST"
            ArsoAlertScope.SLOVENIA_SW -> "SLOVENIA_SOUTH-WEST"
            ArsoAlertScope.SLOVENIA_SE -> "SLOVENIA_SOUTH-EAST"
        }

        val url = "$BASE_URL/uploads/probase/www/warning/text/sl/warning_${scopeText}_latest_CAP.xml"
        val responseString = client.getResponseString(url)

        val factory = DocumentBuilderFactory.newInstance()
        val builder = factory.newDocumentBuilder()
        val source = InputSource(StringReader(responseString))
        val document = builder.parse(source)

        val root = document.documentElement

        var identifier: String? = null
        var sender: String? = null
        var sent: Instant? = null
        var status: String? = null
        var msgType: String? = null
        var scope: String? = null
        val info = mutableListOf<ArsoAlert.Info>()

        for (rootIdx in 0 until root.childNodes.length) {
            val rootElement = root.childElement(rootIdx) ?: continue

            when (rootElement.tagName) {
                "identifier" -> identifier = rootElement.childValue(0)
                "sender" -> sender = rootElement.childValue(0)
                "sent" -> sent = rootElement.childValue(0)?.toInstant()
                "status" -> status = rootElement.childValue(0)
                "msgType" -> msgType = rootElement.childValue(0)
                "scope" -> scope = rootElement.childValue(0)
                "info" -> {
                    var language: String? = null
                    var category: String? = null
                    var event: String? = null
                    var responseType: String? = null
                    var urgency: String? = null
                    var severity: String? = null
                    var certainty: String? = null
                    var effective: Instant? = null
                    var onset: Instant? = null
                    var expires: Instant? = null
                    var senderName: String? = null
                    var headline: String? = null
                    var description: String? = null
                    var instruction: String? = null
                    var web: String? = null
                    val parameters = mutableListOf<ArsoAlert.Info.Parameter>()
                    var area: ArsoAlert.Info.Area? = null

                    for (infoIdx in 0 until rootElement.childNodes.length) {
                        val infoElement = rootElement.childElement(infoIdx) ?: continue

                        when (infoElement.tagName) {
                            "language" -> language = infoElement.childValue(0)
                            "category" -> category = infoElement.childValue(0)
                            "event" -> event = infoElement.childValue(0)
                            "responseType" -> responseType = infoElement.childValue(0)
                            "urgency" -> urgency = infoElement.childValue(0)
                            "severity" -> severity = infoElement.childValue(0)
                            "certainty" -> certainty = infoElement.childValue(0)
                            "effective" -> effective = infoElement.childValue(0)?.toInstant()
                            "onset" -> onset = infoElement.childValue(0)?.toInstant()
                            "expires" -> expires = infoElement.childValue(0)?.toInstant()
                            "senderName" -> senderName = infoElement.childValue(0)
                            "headline" -> headline = infoElement.childValue(0)
                            "description" -> description = infoElement.childValue(0) ?: ""
                            "instruction" -> instruction = infoElement.childValue(0) ?: ""
                            "web" -> web = infoElement.childValue(0)
                            "parameter" -> {
                                var key: String? = null
                                var value: String? = null

                                for (paramIdx in 0 until infoElement.childNodes.length) {
                                    val paramElement = infoElement.childElement(paramIdx) ?: continue

                                    when (paramElement.tagName) {
                                        "valueName" -> key = paramElement.childValue(0)
                                        "value" -> value = paramElement.childValue(0)
                                    }
                                }

                                parameters += ArsoAlert.Info.Parameter(key!!, value!!)
                            }
                            "area" -> {
                                var areaDesc: String? = null
                                val geocode = mutableListOf<ArsoAlert.Info.Area.Geocode>()
                                val polygon = mutableListOf<String>()

                                for (areaIdx in 0 until infoElement.childNodes.length) {
                                    val areaElement = infoElement.childElement(areaIdx) ?: continue

                                    when (areaElement.tagName) {
                                        "areaDesc" -> areaDesc = areaElement.childValue(0)
                                        "geocode" -> {
                                            var key: String? = null
                                            var value: String? = null

                                            for (geocodeIdx in 0 until areaElement.childNodes.length) {
                                                val geocodeElement = areaElement.childElement(geocodeIdx) ?: continue

                                                when (geocodeElement.tagName) {
                                                    "valueName" -> key = geocodeElement.childValue(0)
                                                    "value" -> value = geocodeElement.childValue(0)
                                                }
                                            }

                                            geocode += ArsoAlert.Info.Area.Geocode(key!!, value!!)
                                        }
                                        "polygon" -> {
                                            polygon += areaElement.childValue(0) ?: ""
                                        }
                                    }
                                }

                                area = ArsoAlert.Info.Area(areaDesc!!, geocode, polygon)
                            }
                        }
                    }

                    info += ArsoAlert.Info(
                        language!!, category!!, event!!, responseType!!,
                        urgency!!, severity!!, certainty!!, effective!!,
                        onset!!, expires!!, senderName!!, headline!!,
                        description!!, instruction!!, web!!, parameters, area!!
                    )
                }
            }
        }

        return ArsoAlert(identifier!!, sender!!, sent!!, status!!, msgType!!, scope!!, info)
    }
}

//val Element.numberOfNodes: Int
//    get() = childNodes.length

fun main() {
    val client = OkHttpClient()
    val arso = Arso(client)

    val imageUrls1 = arso.getRadarImageUrls(
        ArsoRadarLength.SHORT,
        ArsoRadarScope.NEIGHBOURS,
    )
    println(imageUrls1.joinToString("\n"))

    val imageUrls2 = arso.getSatelliteImageUrls(
        ArsoSatelliteLength.LONG,
        ArsoSatelliteScope.SLOVENIA,
    )
    println(imageUrls2.joinToString("\n"))

    val imageUrls3 = arso.getAladinImageUrls(
        ArsoAladinScope.ALPS_ADRIATIC,
        ArsoAladinMode.RAIN,
    )
    println(imageUrls3.joinToString("\n"))

    val cameraData = arso.getCameraImageData()
    println(cameraData.joinToString("\n") { it.title })

    val randomCameraData = cameraData.random()
    val cameraImages = arso.getCameraImageUrls(
        randomCameraData,
        randomCameraData.orientations.random(),
        ArsoCameraLength.LONG,
    )
    println(cameraImages.joinToString("\n"))

    return

//    val categoryGroups = arso.getArsoCategoryGroups()
//    println(categoryGroups.joinToString("\n"))
//
//    println("\n")
//
//    val dataGroups = arso.getDataGroups(categoryGroups[0].items[0])
//    println(dataGroups.joinToString("\n"))
}
