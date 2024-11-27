package eu.slickbot.scrape.utils

import eu.slickbot.scrape.utils.extension.getHeaderValue
import eu.slickbot.scrape.utils.extension.replacePrefix
import okhttp3.Interceptor
import okhttp3.Response

class ForceSecureRedirectInterceptor : Interceptor {

  override fun intercept(chain: Interceptor.Chain): Response {
    var request = chain.request()
    var response = chain.proceed(request)

    while (response.code == 301) {
      response.close() // close previous call

      val location = response.getHeaderValue("location", ignoreCase = true) ?: return response
      val url = location.replacePrefix("http://", "https://")

      request = request.newBuilder().url(url).build()
      response = chain.proceed(request)
    }

    return response
  }

}
