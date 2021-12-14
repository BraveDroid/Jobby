package com.bravedroid.jobby.auth.di

import com.bravedroid.jobby.auth.AnalyticsInterceptor
import com.bravedroid.jobby.auth.AuthServiceConstants.BASE_URL
import com.bravedroid.jobby.auth.JobbyAuthenticator
import com.bravedroid.jobby.auth.JobbyInterceptor
import com.bravedroid.jobby.auth.datasource.AuthDataSource
import com.bravedroid.jobby.auth.datasource.TokenProvider
import com.bravedroid.jobby.auth.service.AuthService
import com.bravedroid.jobby.auth.service.UserService
import com.bravedroid.jobby.domain.log.Logger
import com.bravedroid.jobby.logger.NetworkLogger
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [NetworkBuilderHiltModule::class])
@InstallIn(SingletonComponent::class)
abstract class NetworkHiltModule

@Module
@InstallIn(SingletonComponent::class)
class NetworkBuilderHiltModule {
    @Singleton
    @Provides
    fun providesOkHttpClient(
        networkLogger: NetworkLogger,
        jobbyInterceptor: JobbyInterceptor,
        analyticsInterceptor: AnalyticsInterceptor,
        jobbyAuthenticator: JobbyAuthenticator,
    ): OkHttpClient =
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

    @Singleton
    @Provides
    @NetworkQualifiers
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
    fun providesAuthService(@NetworkQualifiers retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    @Singleton
    @Provides
    fun providesUserService(@NetworkQualifiers retrofit: Retrofit): UserService =
        retrofit.create(UserService::class.java)
}
