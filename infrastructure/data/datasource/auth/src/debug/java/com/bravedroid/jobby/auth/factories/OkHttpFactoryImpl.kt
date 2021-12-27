package com.bravedroid.jobby.auth.factories

import android.content.Context
import com.bravedroid.jobby.auth.interceptors.AnalyticsInterceptor
import com.bravedroid.jobby.auth.interceptors.JobbyAuthenticator
import com.bravedroid.jobby.auth.interceptors.JobbyInterceptor
import com.bravedroid.jobby.logger.NetworkLogger
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class OkHttpFactoryImpl @Inject constructor(
    private val networkLogger: NetworkLogger,
    private val jobbyInterceptor: JobbyInterceptor,
    private val analyticsInterceptor: AnalyticsInterceptor,
    private val jobbyAuthenticator: JobbyAuthenticator,
    @ApplicationContext private val context: Context,
) : OkHttpFactory(context) {
    override fun create(): OkHttpClient =
        OkHttpClient.Builder()
            .setSystemCache()
            .connectTimeout(1, TimeUnit.SECONDS)
            .writeTimeout(1, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.SECONDS)
            .callTimeout(1, TimeUnit.SECONDS)
            .addInterceptor(analyticsInterceptor)
            .addInterceptor(jobbyInterceptor)
            .authenticator(jobbyAuthenticator)
            .addInterceptor(networkLogger.applicationLoggingInterceptor)
            .addNetworkInterceptor(networkLogger.networkLoggingInterceptor)
            .build()
}
