package com.bravedroid.jobby.auth.di

import com.bravedroid.jobby.auth.AuthServiceConstants.BASE_URL
import com.bravedroid.jobby.auth.AuthenticationInterceptor
import com.bravedroid.jobby.auth.datasource.TokenProvider
import com.bravedroid.jobby.auth.service.AuthService
import com.bravedroid.jobby.auth.service.UserService
import com.bravedroid.jobby.logger.NetworkLogger
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [OkHttpClientBuilderHiltModule::class])
@InstallIn(SingletonComponent::class)
abstract class OkHttpClientHiltModule

@Module
@InstallIn(SingletonComponent::class)
class OkHttpClientBuilderHiltModule {
    @Singleton
    @Provides
    fun providesOkHttpClient(
        networkLogger: NetworkLogger,
        tokenProvider: TokenProvider,
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(AuthenticationInterceptor(tokenProvider))
            .addInterceptor(networkLogger.applicationLoggingInterceptor)
            .addNetworkInterceptor(networkLogger.networkLoggingInterceptor)
            .build()

    @Singleton
    @Provides
    @JobbyServerRetrofit
    fun providesJobbyServerRetrofit(
        okHttpClient: OkHttpClient,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }.asConverterFactory("application/json".toMediaType()))
        .build()


    @Singleton
    @Provides
    fun providesAuthService(@JobbyServerRetrofit retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    @Singleton
    @Provides
    fun providesUserService(@JobbyServerRetrofit retrofit: Retrofit): UserService =
        retrofit.create(UserService::class.java)
}
