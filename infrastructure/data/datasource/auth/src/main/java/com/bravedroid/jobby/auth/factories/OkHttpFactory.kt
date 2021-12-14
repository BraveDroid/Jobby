package com.bravedroid.jobby.auth.factories

import okhttp3.OkHttpClient

abstract class OkHttpFactory {
    abstract fun create(): OkHttpClient
}
