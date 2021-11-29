package com.bravedroid.jobby.auth.service

import com.bravedroid.jobby.auth.AuthServiceConstants.BASE_URL
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class NetworkBuilderHiltModule {
    private fun providesOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            // TODO: 09/11/2021 RF: support cache and network interceptor for debugging tools, headers...
            .build()


    private fun providesRetrofit(
        okHttpClient: OkHttpClient,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }.asConverterFactory("application/json".toMediaType()))
        .build()


    fun providesAuthService(): AuthService {
        val retrofit = providesRetrofit(providesOkHttpClient())
        return retrofit.create(AuthService::class.java)
    }
}
