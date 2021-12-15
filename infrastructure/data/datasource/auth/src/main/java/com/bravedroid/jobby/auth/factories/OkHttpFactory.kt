package com.bravedroid.jobby.auth.factories

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Cache
import okhttp3.OkHttpClient

abstract class OkHttpFactory(
    @ApplicationContext private val context: Context,
) {
    companion object {
        private const val OK_HTTP_CACHE_SIZE = 150L * 1024 * 1024 // 150MB
    }

    abstract fun create(): OkHttpClient
    protected fun OkHttpClient.Builder.setSystemCache() = this.cache(getSystemCache())
    private fun getSystemCache() = Cache(context.cacheDir, OK_HTTP_CACHE_SIZE)
}
