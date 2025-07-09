package com.example.movieapp.data.remote

import okhttp3.Interceptor
import okhttp3.Response
import okio.IOException

class RetryInterceptor(private val maxTries: Int = 3) : Interceptor {
    private val API_KEY = "e46c420ee3e515c5c7404e15505bb5da"
    override fun intercept(chain: Interceptor.Chain): Response {
        var attempt = 0
        var lastException: IOException? = null
        while (attempt < maxTries) {
            try {
                val original = chain.request()
                val url = original.url.newBuilder()
                    .addQueryParameter("api_key", API_KEY).build()
                val request = original.newBuilder().url(url).build()
                return chain.proceed(request)
            } catch (e: IOException) {
                lastException = e
                attempt++
            }
        }
        throw lastException ?: IOException("Unknown error from server")
    }
}