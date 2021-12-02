package com.bravedroid.jobby.auth.di

import com.bravedroid.jobby.auth.AuthenticationInterceptor
import com.bravedroid.jobby.auth.TokenProvider
import com.bravedroid.jobby.logger.NetworkLogger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
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
}
