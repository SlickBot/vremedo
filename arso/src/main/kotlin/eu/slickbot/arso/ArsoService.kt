package eu.slickbot.arso

import eu.slickbot.arso.model.ArsoLocationInfo
import retrofit2.http.GET
import retrofit2.http.Query

interface ArsoService {

  @GET("location/")
  suspend fun locationInfo(
    @Query("lang") lang: String,
    @Query("location") location: String,
  ): ArsoLocationInfo

}
