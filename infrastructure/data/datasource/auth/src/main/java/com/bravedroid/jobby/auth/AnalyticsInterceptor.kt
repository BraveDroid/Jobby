package com.bravedroid.jobby.auth

import okhttp3.Headers.Companion.toHeaders
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class AnalyticsInterceptor @Inject constructor(
    private val appMetadataAnalyticsProvider: AppMetadataAnalyticsProvider
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val requestBuilder: Request.Builder = request.newBuilder()
        val metaData = appMetadataAnalyticsProvider.provideMetaData()
        requestBuilder.headers(metaData.toHeaders())
        return chain.proceed(requestBuilder.build())
    }
}
