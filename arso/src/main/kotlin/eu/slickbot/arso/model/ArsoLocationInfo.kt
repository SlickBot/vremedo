package eu.slickbot.arso.model

import com.google.gson.annotations.SerializedName

data class ArsoLocationInfo(
    val forecast1h: Forecast,
    val forecast3h: Forecast,
    val forecast6h: Forecast,
    val forecast24h: Forecast,
)

data class Forecast(
    val type: String,
    val dataType: String,
    val features: List<Feature>,
    val language: String,
    val params: Params,
    val tsUpdated: String,
    val tsValid: String,
    @SerializedName("icon_base_url")
    val iconBaseUrl: String,
    @SerializedName("icon_format")
    val iconFormat: String,
) {

    data class Feature(
        val type: String,
        val geometry: Geometry,
        val properties: Properties,
    ) {

        data class Geometry(
            val type: String,
            val coordinates: List<Double>,
        )

        data class Properties(
            val id: String,
            val parentId: String,
            val title: String,
            val country: String,
            val days: List<Day>,
        ) {

            data class Day(
                val date: String,
                val sunrise: String,
                val sunset: String,
                val timeline: List<Timeline>,
                @SerializedName("UTCoffset")
                val utcOffset: String,
            ) {

                data class Timeline(
                    @SerializedName("cloudBase_shortText")
                    val cloudBaseShortText: String,
                    @SerializedName("clouds_icon_wwsyn_icon")
                    val cloudsIconWwsynIcon: String,
                    @SerializedName("clouds_shortText")
                    val cloudsShortText: String,
                    @SerializedName("clouds_shortText_wwsyn_shortText")
                    val cloudsShortTextWwsynShortText: String,
                    @SerializedName("dd_shortText")
                    val ddShortText: String,
                    @SerializedName("ddff_icon")
                    val ddffIcon: String,
                    @SerializedName("ff_shortText")
                    val ffShortText: String,
                    @SerializedName("ff_val")
                    val ffVal: String,
                    @SerializedName("ffmax_val")
                    val ffMaxVal: String,
                    @SerializedName("interval")
                    val interval: String,
                    @SerializedName("msl")
                    val msl: String,
                    @SerializedName("pa_shortText")
                    val paShortText: String,
                    @SerializedName("rh")
                    val rh: String,
                    @SerializedName("rh_shortText")
                    val rhShortText: String,
                    @SerializedName("sn_acc")
                    val snAcc: String,
                    @SerializedName("t")
                    val t: String,
                    @SerializedName("time")
                    val time: String,
                    @SerializedName("tp_acc")
                    val tpAcc: String,
                    @SerializedName("valid")
                    val valid: String,
                    @SerializedName("wwsyn_decodeText")
                    val wwsynDecodeText: String,
                    @SerializedName("wwsyn_icon")
                    val wwsynIcon: String,
                    @SerializedName("wwsyn_shortText")
                    val wwsynShortText: String,
                )
            }
        }
    }

    data class Params(
        @SerializedName("cloudBase_shortText")
        val cloudBaseShortText: Param,
        @SerializedName("clouds_icon_wwsyn_icon")
        val cloudsIconWwsynIcon: Param,
        @SerializedName("clouds_shortText")
        val cloudsShortText: Param,
        @SerializedName("dd_shortText")
        val ddShortText: Param,
        @SerializedName("ddff_icon")
        val ddffIcon: Param,
        @SerializedName("ff_shortText")
        val ffShortText: Param,
        @SerializedName("ff_val")
        val ffVal: Param,
        @SerializedName("ffmax_val")
        val ffmaxVal: Param,
        @SerializedName("msl")
        val msl: Param,
        @SerializedName("pa_shortText")
        val paShortText: Param,
        @SerializedName("rh")
        val rh: Param,
        @SerializedName("rh_shortText")
        val rhShortText: Param,
        @SerializedName("sn_acc")
        val snAcc: Param,
        @SerializedName("t")
        val t: Param,
        @SerializedName("tp_acc")
        val tpAcc: Param,
        @SerializedName("wwsyn_decodeText")
        val wwsynDecodeText: Param,
        @SerializedName("wwsyn_icon")
        val wwsynIcon: Param,
        @SerializedName("wwsyn_shortText")
        val wwsynShortText: Param,
    ) {

        data class Param(
            val desc: String,
            val name: String,
            val unit: String,
        )
    }

}
