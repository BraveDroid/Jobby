package com.bravedroid.jobby.logger

import android.content.Context
import com.bravedroid.jobby.domain.log.Logger
import com.bravedroid.jobby.domain.log.Priority
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import javax.inject.Inject

class TimberLogger @Inject constructor(
    @ApplicationContext private val context: Context,
) : Logger, NetworkLogger {
    override fun log(tag: String, msg: String, priority: Priority) = with(Timber.tag(tag)) {
        when (priority) {
            Priority.V -> v(msg)
            Priority.D -> d(msg)
            Priority.I -> i(msg)
            Priority.W -> w(msg)
            Priority.E -> e(msg)
        }
    }

    override fun log(tag: String, t: Throwable) {
        Timber.tag(tag).e(t)
    }

    override fun init() {
        Timber.plant(Timber.DebugTree())
        Stetho.initializeWithDefaults(context)
    }

    override val networkLoggingInterceptor: Interceptor = StethoInterceptor()

    override val applicationLoggingInterceptor: Interceptor = HttpLoggingInterceptor {
        log(tag = "HttpLoggingInterceptor", msg = it)
    }.apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }
}

