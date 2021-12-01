package com.bravedroid.jobby.logger

import okhttp3.Interceptor

interface NetworkLogger {
    val networkLoggingInterceptor: Interceptor
    val applicationLoggingInterceptor: Interceptor
}
