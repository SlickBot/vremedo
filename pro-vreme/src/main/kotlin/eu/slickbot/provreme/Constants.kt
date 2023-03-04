package eu.slickbot.provreme

internal object Constants {

    const val API_BASE_URL = "https://www.pro-vreme.net"
    const val API_INDEX_URL = "$API_BASE_URL/index.php"
    const val API_WEEK_URL = "$API_INDEX_URL?id=2000"
    const val API_DAY_URL = "$API_INDEX_URL?id=2001"

}
