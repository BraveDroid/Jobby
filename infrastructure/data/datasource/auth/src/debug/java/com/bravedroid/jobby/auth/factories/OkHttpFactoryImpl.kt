package com.bravedroid.jobby.auth.factories

import com.bravedroid.jobby.auth.interceptors.AnalyticsInterceptor
import com.bravedroid.jobby.auth.interceptors.JobbyAuthenticator
import com.bravedroid.jobby.auth.interceptors.JobbyInterceptor
import com.bravedroid.jobby.logger.NetworkLogger
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class OkHttpFactoryImpl @Inject constructor(
    private val networkLogger: NetworkLogger,
    private val jobbyInterceptor: JobbyInterceptor,
    private val analyticsInterceptor: AnalyticsInterceptor,
    private val jobbyAuthenticator: JobbyAuthenticator,
) : OkHttpFactory() {
    override fun create(): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .callTimeout(5, TimeUnit.MINUTES)
            .addInterceptor(analyticsInterceptor)
            .addInterceptor(jobbyInterceptor)
            .authenticator(jobbyAuthenticator)
            .addInterceptor(networkLogger.applicationLoggingInterceptor)
            .addNetworkInterceptor(networkLogger.networkLoggingInterceptor)
            .build()
}
