package eu.slickbot.arso.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ArsoLocationInfo(
  val forecast1h: Forecast?,
  val forecast3h: Forecast?,
  val forecast6h: Forecast?,
  val forecast24h: Forecast?,
)

@JsonClass(generateAdapter = true)
data class Forecast(
  val type: String?,
  val dataType: String?,
  val features: List<Feature> = emptyList(),
  val language: String?,
  val params: Params?,
  val tsUpdated: String?,
  val tsValid: String?,
  @Json(name = "icon_base_url")
  val iconBaseUrl: String?,
  @Json(name = "icon_format")
  val iconFormat: String?,
) {

  @JsonClass(generateAdapter = true)
  data class Feature(
    val type: String?,
    val geometry: Geometry?,
    val properties: Properties?,
  ) {

    @JsonClass(generateAdapter = true)
    data class Geometry(
      val type: String?,
      val coordinates: List<Double> = emptyList(),
    )

    @JsonClass(generateAdapter = true)
    data class Properties(
      val id: String?,
      val parentId: String?,
      val title: String?,
      val country: String?,
      val days: List<Day> = emptyList(),
    ) {

      @JsonClass(generateAdapter = true)
      data class Day(
        val date: String?,
        val sunrise: String?,
        val sunset: String?,
        val timeline: List<Timeline> = emptyList(),
        @Json(name = "UTCoffset")
        val utcOffset: String?,
      ) {

        @JsonClass(generateAdapter = true)
        data class Timeline(
          @Json(name = "cloudBase_shortText")
          val cloudBaseShortText: String?,
          @Json(name = "clouds_icon_wwsyn_icon")
          val cloudsIconWwsynIcon: String?,
          @Json(name = "clouds_shortText")
          val cloudsShortText: String?,
          @Json(name = "clouds_shortText_wwsyn_shortText")
          val cloudsShortTextWwsynShortText: String?,
          @Json(name = "dd_shortText")
          val ddShortText: String?,
          @Json(name = "ddff_icon")
          val ddffIcon: String?,
          @Json(name = "ff_shortText")
          val ffShortText: String?,
          @Json(name = "ff_val")
          val ffVal: String?,
          @Json(name = "ffmax_val")
          val ffMaxVal: String?,
          @Json(name = "interval")
          val interval: String?,
          @Json(name = "msl")
          val msl: String?,
          @Json(name = "pa_shortText")
          val paShortText: String?,
          @Json(name = "rh")
          val rh: String?,
          @Json(name = "rh_shortText")
          val rhShortText: String?,
          @Json(name = "sn_acc")
          val snAcc: String?,
          @Json(name = "t")
          val t: String?,
          @Json(name = "time")
          val time: String?,
          @Json(name = "tp_acc")
          val tpAcc: String?,
          @Json(name = "valid")
          val valid: String?,
          @Json(name = "wwsyn_decodeText")
          val wwsynDecodeText: String?,
          @Json(name = "wwsyn_icon")
          val wwsynIcon: String?,
          @Json(name = "wwsyn_shortText")
          val wwsynShortText: String?,
        )
      }
    }
  }

  @JsonClass(generateAdapter = true)
  data class Params(
    @Json(name = "cloudBase_shortText")
    val cloudBaseShortText: Param?,
    @Json(name = "clouds_icon_wwsyn_icon")
    val cloudsIconWwsynIcon: Param?,
    @Json(name = "clouds_shortText")
    val cloudsShortText: Param?,
    @Json(name = "dd_shortText")
    val ddShortText: Param?,
    @Json(name = "ddff_icon")
    val ddffIcon: Param?,
    @Json(name = "ff_shortText")
    val ffShortText: Param?,
    @Json(name = "ff_val")
    val ffVal: Param?,
    @Json(name = "ffmax_val")
    val ffmaxVal: Param?,
    @Json(name = "msl")
    val msl: Param?,
    @Json(name = "pa_shortText")
    val paShortText: Param?,
    @Json(name = "rh")
    val rh: Param?,
    @Json(name = "rh_shortText")
    val rhShortText: Param?,
    @Json(name = "sn_acc")
    val snAcc: Param?,
    @Json(name = "t")
    val t: Param?,
    @Json(name = "tp_acc")
    val tpAcc: Param?,
    @Json(name = "wwsyn_decodeText")
    val wwsynDecodeText: Param?,
    @Json(name = "wwsyn_icon")
    val wwsynIcon: Param?,
    @Json(name = "wwsyn_shortText")
    val wwsynShortText: Param?,
  ) {

    @JsonClass(generateAdapter = true)
    data class Param(
      val desc: String?,
      val name: String?,
      val unit: String?,
    )
  }
}
