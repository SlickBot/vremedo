package eu.slickbot.arso.model

import kotlinx.datetime.Instant

data class ArsoAlert(
    val identifier: String,
    val sender: String,
    val sent: Instant,
    val status: String,
    val msgType: String,
    val scope: String,
    val info: List<Info>,
) {

    data class Info(
        val language: String,
        val category: String,
        val event: String,
        val responseType: String,
        val urgency: String,
        val severity: String,
        val certainty: String,
        val effective: Instant,
        val onset: Instant,
        val expires: Instant,
        val senderName: String,
        val headline: String,
        val description: String,
        val instruction: String,
        val web: String,
        val parameters: List<Parameter>,
        val area: Area,
    ) {

        data class Parameter(
            val key: String,
            val value: String,
        )

        data class Area(
            val areaDesc: String,
            val geocode: List<Geocode>,
            val polygon: List<String>,
        ) {

            data class Geocode(
                val key: String,
                val value: String,
            )
        }
    }
}
