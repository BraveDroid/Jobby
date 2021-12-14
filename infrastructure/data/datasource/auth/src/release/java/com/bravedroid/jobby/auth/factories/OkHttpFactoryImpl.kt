package com.bravedroid.jobby.auth.factories

import com.bravedroid.jobby.auth.interceptors.AnalyticsInterceptor
import com.bravedroid.jobby.auth.interceptors.JobbyAuthenticator
import com.bravedroid.jobby.auth.interceptors.JobbyInterceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class OkHttpFactoryImpl @Inject constructor(
    private val jobbyInterceptor: JobbyInterceptor,
    private val analyticsInterceptor: AnalyticsInterceptor,
    private val jobbyAuthenticator: JobbyAuthenticator,
) : OkHttpFactory() {
    override fun create(): OkHttpClient =
        OkHttpClient.Builder()
            .callTimeout(2, TimeUnit.MINUTES)
            .connectTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(analyticsInterceptor)
            .addInterceptor(jobbyInterceptor)
            .authenticator(jobbyAuthenticator)
            .build()
}
